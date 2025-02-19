package nnt.com.application.service.booking.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.application.brokerMQ.producer.MailProducer;
import nnt.com.application.service.booking.BookingAppService;
import nnt.com.application.service.homestay.cache.HomestayAppServiceCache;
import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.dto.response.PriceResponse;
import nnt.com.domain.aggregates.service.BookingDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.domain.shared.model.vo.LockKey;
import nnt.com.domain.shared.model.vo.RedisKey;
import nnt.com.infrastructure.cache.local.LocalCache;
import nnt.com.infrastructure.cache.redis.RedisCache;
import nnt.com.infrastructure.distributed.redisson.BloomFilterService;
import nnt.com.infrastructure.distributed.redisson.RedisDistributedLocker;
import nnt.com.infrastructure.distributed.redisson.RedisDistributedService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class BookingAppServiceImpl implements BookingAppService {
    MailProducer mailProducer;
    RedisCache redisCache;
    LocalCache<PriceResponse> localCache;
    RedisDistributedService distributedCache;

    BookingDomainService bookingDomainService;
    BloomFilterService bloomFilterService;
    HomestayAppServiceCache homestayAppServiceCache;

    @Override
    @Transactional
    public String booking(BookingRequest request) {
        int guests = request.getAdult() + request.getChildren() + request.getInfant() / 2;
        validateBookingRequest(request.getCheckIn(), request.getCheckOut(), guests, request.getHomestayId());

        // Problem: nhiều request đặt phòng cùng lúc cho cùng 1 homestay gây xung đột dữ liệu
        // Solution: sử dụng Redis distributed lock để đồng bộ hóa việc xử lý booking
        String key = String.valueOf(request.getHomestayId());
        RedisDistributedLocker locker = distributedCache.getDistributedLocker(LockKey.BOOKING.getKey() + key);
        try {
            boolean isLocked = locker.tryLock(3, 10, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS);
            }
            if (!isRoomAvailable(request)) {
                throw new BusinessException(ErrorCode.NO_ROOM_AVAILABLE);
            }

            String bookingId = saveBookingToDatabase(request);
            sendBookingMessageToKafka(request.getEmail(), "Nguyễn Văn A");
            setAvailableRoomInCache(request.getCheckIn(), request.getCheckOut(), RedisKey.ROOM_AVAILABILITY.getKey() + request.getHomestayId() + ":" + request.getRoomId());
            return bookingId;
        } catch (InterruptedException e) {
            log.error("BOOKING PROCESS INTERRUPTED DUE TO: {}", e.getMessage());
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.UNCATEGORIZED);
        } finally {
            locker.unlock();
        }
    }

    @Override
    public PriceResponse calculatePrice(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomId) {
        validateBookingRequest(checkIn, checkOut, guests, homestayId);

        // Problem: thời gian xử lý tính toán giá phòng lâu gây giảm hiệu suất
        // Solution: sử dụng Local cache và Redis cache để lưu trữ thông tin giá phòng đã tính toán
        PriceResponse response = getPriceFromLocalCache(homestayId, checkIn, checkOut, guests, roomId);
        if (response != null) {
            log.info("GET PRICE FROM LOCAL CACHE");
            return response;
        }

        response = getPriceFromRedisCache(homestayId, checkIn, checkOut, guests, roomId);
        if (response != null) {
            log.info("GET PRICE FROM REDIS CACHE");
            return response;
        }

        log.info("CALCULATE PRICE FROM DOMAIN SERVICE");
        response = bookingDomainService.calculatePrice(homestayId, checkIn, checkOut, guests, roomId);
        setPriceToCache(homestayId, checkIn, checkOut, guests, roomId, response);
        return response;
    }

    private PriceResponse getPriceFromLocalCache(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomId) {
        String stringKey = RedisKey.PRICE.getKey() + homestayId + ":" + checkIn + ":" + checkOut + ":" + guests + ":" + roomId;
        long key = stringKey.hashCode();
        return localCache.getIfPresent(key);
    }

    private PriceResponse getPriceFromRedisCache(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomId) {
        String key = RedisKey.PRICE.getKey() + homestayId + ":" + checkIn + ":" + checkOut + ":" + guests + ":" + roomId;
        return redisCache.getObject(key, PriceResponse.class);
    }

    private void setPriceToCache(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomId, PriceResponse response) {
        String redisKey = RedisKey.PRICE.getKey() + homestayId + ":" + checkIn + ":" + checkOut + ":" + guests + ":" + roomId;
        redisCache.setObject(redisKey, response, 1L, TimeUnit.MINUTES);
        long localKey = redisKey.hashCode();
        localCache.put(localKey, response);
        log.info("SAVE PRICE TO LOCAL AND REDIS CACHE");
    }

    private void validateBookingRequest(LocalDate checkIn, LocalDate checkOut, int guests, long homestayId) {
        if (checkIn.isBefore(LocalDate.now()) || checkOut.isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.CHECKIN_CHECKOUT_IN_PAST);
        }
        if (checkIn.isAfter(checkOut)) {
            throw new BusinessException(ErrorCode.CHECKIN_AFTER_CHECKOUT);
        }
        HomestayResponse homestay = homestayAppServiceCache.getHomestayById(homestayId); // get by cache to save time
        if (guests > homestay.getMaxGuests()) {
            throw new BusinessException(ErrorCode.MAX_GUESTS_EXCEEDED);
        }
    }

    // Problem: nhiều request cùng lúc, việc kiểm tra room available gây áp lực lớn cho database
    // Solution: sử dụng Redis cache để lưu trữ thông tin room available
    private boolean isRoomAvailable(BookingRequest request) {
        if (request.getRoomId() == 0) {
            return true;
        }
        long roomId = request.getRoomId();
        LocalDate checkIn = request.getCheckIn();
        LocalDate checkOut = request.getCheckOut();
        String key = RedisKey.ROOM_AVAILABILITY.getKey() + request.getHomestayId() + ":" + roomId;

        // Step 1: Check bloom filter first, not found means room has not been booked
        if (!hasRoomAvailableInCacheWithBloomFilter(checkIn, checkOut, key)) {
            return true;
        }

        // Step 2: If bloom filter suggests room might be booked, check database
        boolean available = bookingDomainService.isRoomAvailable(roomId, checkIn, checkOut);
        log.info("ROOM AVAILABLE IN RANGE {} TO {} IS {}", checkIn, checkOut, available);

        return available;
    }

    // Problem: truy vấn Redis nhiều lần trong vòng lặp gây giảm hiệu suất
    // Solution: sử dụng Bloom Filter -> tăng tốc độ truy vấn khi tìm kiếm key không tồn tại
    public boolean hasRoomAvailableInCacheWithBloomFilter(LocalDate checkIn, LocalDate checkOut, String key) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (LocalDate i = checkIn; i.isBefore(checkOut); i = i.plusDays(1)) {
            String fullKey = key + ":" + i.format(formatter);
            if (bloomFilterService.mightContain(fullKey)) {
                log.info("BLOOM FILTER SUGGESTS ROOM AVAILABILITY MIGHT BE FOUND IN DATE {}, CHECK AGAIN IN DATABASE", i);
                return true;
            }
        }

        log.info("BLOOM FILTER CONFIRMS ROOM AVAILABILITY NOT FOUND IN RANGE {} TO {}", checkIn, checkOut);
        return false;
    }

    public void setAvailableRoomInCache(LocalDate checkIn, LocalDate checkOut, String key) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (LocalDate i = checkIn; i.isBefore(checkOut); i = i.plusDays(1)) {
            String fullKey = key + ":" + i.format(formatter);
            bloomFilterService.add(fullKey);
            log.info("ROOM AVAILABLE SAVED IN Redis FOR KEY {}", fullKey);
        }
    }

    private String saveBookingToDatabase(BookingRequest request) {
        log.info("SAVE BOOKING TO DATABASE");
        return bookingDomainService.booking(request);
    }

    private void sendBookingMessageToKafka(String email, String name) {
        mailProducer.sendBookingMail(email, name);
        log.info("SEND BOOKING MESSAGE TO KAFKA: {}", email);
    }
}
