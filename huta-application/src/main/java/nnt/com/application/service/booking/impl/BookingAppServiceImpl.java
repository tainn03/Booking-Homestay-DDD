package nnt.com.application.service.booking.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.application.brokerMQ.producer.MailProducer;
import nnt.com.application.service.booking.BookingAppService;
import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.service.BookingDomainService;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.domain.shared.model.vo.LockKey;
import nnt.com.domain.shared.model.vo.RedisKey;
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
    RedisCache redisCache;
    RedisDistributedService distributedCache;
    MailProducer mailProducer;
    BookingDomainService bookingDomainService;
    HomestayDomainService homestayDomainService;
    BloomFilterService bloomFilterService;

    @Override
    @Transactional
    public void booking(BookingRequest request) {
        validateBookingRequest(request);

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

            saveBookingToDatabase(request);
            sendBookingMessageToKafka(request.getEmail(), "Nguyễn Văn A");
            setAvailableRoomInCache(request.getCheckIn(), request.getCheckOut(), RedisKey.ROOM_AVAILABILITY.getKey() + request.getHomestayId() + ":" + request.getRoomId());
        } catch (InterruptedException e) {
            log.error("BOOKING PROCESS INTERRUPTED DUE TO: {}", e.getMessage());
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.UNCATEGORIZED);
        } finally {
            locker.unlock();
        }
    }

    private void validateBookingRequest(BookingRequest request) {
        if (request.getCheckIn().isBefore(LocalDate.now()) || request.getCheckOut().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.CHECKIN_CHECKOUT_IN_PAST);
        }
        if (request.getCheckIn().isAfter(request.getCheckOut())) {
            throw new BusinessException(ErrorCode.CHECKIN_AFTER_CHECKOUT);
        }
        Homestay homestay = homestayDomainService.getById(request.getHomestayId());
        int guests = request.getAdult() + request.getChildren() + request.getInfant() / 2; // 1 infant = 0.5 guest
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

        // Step 1: Check cache first
        if (hasRoomAvailableInCacheWithBloomFilter(checkIn, checkOut, key)) {
            return false;
        }

        // Step 2: If not in cache, check database
        boolean available = bookingDomainService.isRoomAvailable(roomId, checkIn, checkOut);
        log.info("RETURN {} WHEN GET ROOM AVAILABILITY FROM DATABASE FOR ROOM {} WITH CHECKIN {} AND CHECKOUT {}", available, roomId, checkIn, checkOut);

        return available;
    }

    // Problem: truy vấn Redis nhiều lần trong vòng lặp gây giảm hiệu suất
    // Solution: sử dụng Bloom Filter -> tăng tốc độ truy vấn khi tìm kiếm key không tồn tại
    public boolean hasRoomAvailableInCacheWithBloomFilter(LocalDate checkIn, LocalDate checkOut, String key) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (LocalDate i = checkIn; i.isBefore(checkOut); i = i.plusDays(1)) {
            String fullKey = key + ":" + i.format(formatter);

            // Step 1: Check Bloom Filter first
            if (!bloomFilterService.mightContain(fullKey)) {
                log.info("BLOOM FILTER CONFIRMS ROOM AVAILABILITY NOT FOUND FOR KEY {}", fullKey);
                return false;
            }

            // Step 2: Check Redis if Bloom Filter suggests key might exist
            if (redisCache.hasKey(fullKey)) {
                log.info("ROOM AVAILABLE FOUND IN Redis FOR KEY {}", fullKey);
                return true;
            }
        }

        log.info("ROOM AVAILABILITY NOT FOUND IN REDIS FOR KEY {}", key);
        return false;
    }

    public void setAvailableRoomInCache(LocalDate checkIn, LocalDate checkOut, String key) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (LocalDate i = checkIn; i.isBefore(checkOut); i = i.plusDays(1)) {
            String fullKey = key + ":" + i.format(formatter);
            redisCache.setObject(fullKey, false, 1L, TimeUnit.MINUTES);
            bloomFilterService.add(fullKey);
            log.info("ROOM AVAILABLE SAVED IN Redis FOR KEY {}", fullKey);
        }
    }

    private void saveBookingToDatabase(BookingRequest request) {
        bookingDomainService.booking(request);
        log.info("SAVE BOOKING TO DATABASE");
    }

    private void sendBookingMessageToKafka(String email, String name) {
        mailProducer.sendBookingMail(email, name);
        log.info("SEND BOOKING MESSAGE TO KAFKA: {}", email);
    }
}
