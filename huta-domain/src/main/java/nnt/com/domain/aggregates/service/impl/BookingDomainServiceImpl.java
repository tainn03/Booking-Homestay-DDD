package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.aggregates.model.dto.response.BookingResponse;
import nnt.com.domain.aggregates.model.dto.response.PaymentResponse;
import nnt.com.domain.aggregates.model.dto.response.PriceResponse;
import nnt.com.domain.aggregates.model.entity.*;
import nnt.com.domain.aggregates.model.mapper.BookingMapper;
import nnt.com.domain.aggregates.model.mapper.PaymentMapper;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import nnt.com.domain.aggregates.model.vo.DiscountType;
import nnt.com.domain.aggregates.repository.*;
import nnt.com.domain.aggregates.service.BookingDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.domain.shared.utils.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class BookingDomainServiceImpl implements BookingDomainService {
    BookingDomainRepository bookingDomainRepository;
    HomestayDomainRepository homestayDomainRepository;
    RoomAvailableDomainRepository roomAvailableDomainRepository;
    UserDomainRepository userDomainRepository;
    RoomDomainRepository roomDomainRepository;
    BookingMapper bookingMapper;
    PaymentMapper paymentMapper;

    @Override
    public Booking save(Booking booking) {
        return bookingDomainRepository.save(booking);
    }

    @Override
    public Booking update(Booking booking) {
        return bookingDomainRepository.save(booking);
    }

    @Override
    public Booking getById(Long id) {
        return bookingDomainRepository.getById(id);
    }

    @Override
    public Page<Booking> getAll(int page, int size, String sort, String direction) {
        return bookingDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        bookingDomainRepository.delete(id);
    }

    @Override
    public void booking(BookingRequest request, String code) {
        Homestay homestay = homestayDomainRepository.getById(request.getHomestayId());
        int guests = request.getAdult() + request.getChildren() + request.getInfant() / 2;

        Room selectedRoom = getRoomAvailable(homestay, request.getRoomId(), guests, request.getCheckIn(), request.getCheckOut());

        saveBookingToDB(request, selectedRoom, code);
        saveAvailableRoomToDB(selectedRoom, request.getCheckIn(), request.getCheckOut());
    }

    private void saveBookingToDB(BookingRequest request, Room selectedRoom, String code) {
        int nights = (int) ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        Booking savedBooking = bookingDomainRepository.save(Booking.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .code(code)
                .note(request.getNote())
                .totalCost(request.getTotalCost())
                .night(nights)
                .adults(request.getAdult())
                .children(request.getChildren())
                .infants(request.getInfant())
                .status(BookingStatus.PENDING)
                .user(userDomainRepository.getByEmail(request.getEmail()))
                .rooms(List.of(selectedRoom))
                .build());
        log.info("SAVE BOOKING: {}", savedBooking.getCode());
    }

    private void saveAvailableRoomToDB(Room room, LocalDate checkIn, LocalDate checkOut) {
        for (LocalDate i = checkIn; i.isBefore(checkOut); i = i.plusDays(1)) {
            RoomAvailable roomAvailable = RoomAvailable.builder()
                    .room(room)
                    .date(i)
                    .available(0)
                    .build();
            roomAvailableDomainRepository.save(roomAvailable);
            log.info("SAVE AVAILABLE ROOM: {}", roomAvailable.getId());
        }
    }

    private Room getRoomAvailable(Homestay homestay, long roomId, int guests, LocalDate checkIn, LocalDate checkOut) {
        if (roomId == 0) {
            return getSuitableRoom(homestay, guests, checkIn, checkOut);
        }
        return getSelectedRoom(homestay, roomId);
    }

    private Room getSuitableRoom(Homestay homestay, int guests, LocalDate checkIn, LocalDate checkOut) {
        List<Room> suitableRooms = new ArrayList<>();
        homestay.getRooms().forEach(room -> {
            if (isRoomAvailable(room.getId(), checkIn, checkOut) && room.getSize() >= guests) {
                suitableRooms.add(room);
            }
        });
        if (suitableRooms.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_ROOM_AVAILABLE);
        }
        log.info("GET SUITABLE ROOMS: {}", suitableRooms);
        return suitableRooms.stream().min(Comparator.comparing(Room::getSize))
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_ROOM_AVAILABLE));
    }

    private Room getSelectedRoom(Homestay homestay, long roomIds) {
        log.info("GET SELECTED ROOM: {}", roomIds);
        return homestay.getRooms().stream()
                .filter(room -> room.getId() == roomIds)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
    }

    @Override
    public boolean isRoomAvailable(long roomId, LocalDate checkIn, LocalDate checkOut) {
        return roomAvailableDomainRepository.isRoomAvailable(roomId, checkIn, checkOut);
    }

    @Override
    public void cleanBookingExpiration() {
        List<Booking> pendingBookings = bookingDomainRepository.getByStatus(BookingStatus.PENDING);
        pendingBookings.forEach(booking -> {
            if (booking.getCreatedAt().isBefore(LocalDateTime.now().minusHours(1))) {
                LocalDate checkIn = booking.getCheckIn();
                LocalDate checkOut = booking.getCheckOut();
                List<Room> bookedRooms = booking.getRooms();
                bookedRooms.forEach(room -> {
                    List<RoomAvailable> availables = room.getRoomAvailables();
                    availables.forEach(available -> {
                        if (available.getDate().isAfter(checkIn.minusDays(1)) && available.getDate().isBefore(checkOut)) {
                            available.setAvailable(1);
                            roomAvailableDomainRepository.save(available);
                        }
                    });
                });
                booking.setStatus(BookingStatus.EXPIRED);
                bookingDomainRepository.update(booking);
            }
        });
    }

    @Override
    public PriceResponse calculatePrice(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomId) {
        long totalCost = 0;
        long originalCost = 0;
        long discountValue = 0;
        int dailyDays = 0;
        int weekendDays = 0;

        // get room and discounts
        Room room = roomDomainRepository.getById(roomId);
        Discount monthlyDiscount = getMonthlyDiscount(room);
        Discount customDiscount = getCustomDiscount(room, checkIn, checkOut);

        // map through each day to calculate price
        for (LocalDate i = checkIn; i.isBefore(checkOut); i = i.plusDays(1)) {
            boolean isWeekday = i.getDayOfWeek().getValue() < 6;
            dailyDays += isWeekday ? 1 : 0;
            weekendDays += isWeekday ? 0 : 1;
            originalCost += isWeekday ? room.getDailyPrice() : room.getWeekendPrice();
            totalCost += isWeekday ? room.getDailyPrice() : room.getWeekendPrice();

            // check custom discount and apply
            if (customDiscount != null && isDayInPeriod(i, customDiscount.getStartDate().toLocalDate(), customDiscount.getEndDate().toLocalDate())) {
                discountValue += (long) (totalCost * customDiscount.getValue() / 100);
                totalCost = (long) (totalCost * (1 - customDiscount.getValue() / 100));
                log.info("APPLY DAILY DISCOUNT: {}", customDiscount.getValue());
            }
        }

        // check monthly discount and apply
        if (ChronoUnit.DAYS.between(checkIn, checkOut) > 30 && monthlyDiscount != null) {
            log.info("APPLY MONTHLY DISCOUNT: {}", monthlyDiscount.getValue());
            discountValue += (long) (totalCost * monthlyDiscount.getValue() / 100);
            totalCost = (long) (totalCost * (1 - monthlyDiscount.getValue() / 100));
        }

        return PriceResponse.builder()
                .originalCost(formatCurrency(originalCost))
                .discountValue(formatCurrency(discountValue))
                .totalCost(formatCurrency(totalCost))
                .dailyDays(dailyDays)
                .dailyPrice(formatCurrency(room.getDailyPrice()))
                .weekendDays(weekendDays)
                .weekendPrice(formatCurrency(room.getWeekendPrice()))
                .build();
    }

    @Override
    public Booking getByCode(String orderInfo) {
        return bookingDomainRepository.getByCode(orderInfo);
    }

    @Override
    public BookingResponse getBookingByCode(String code) {
        Booking booking = bookingDomainRepository.getByCode(code);
        if (booking == null) {
            throw new BusinessException(ErrorCode.BOOKING_NOT_FOUND);
        }
        return getBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByHomestay(long homestayId) {
        List<Booking> bookings = bookingDomainRepository.getByHomestayId(homestayId);
        List<BookingResponse> responses = new ArrayList<>();
        bookings.forEach(booking -> {
            BookingResponse response = getBookingResponse(booking);
            responses.add(response);
        });
        responses.sort(Comparator.comparing(BookingResponse::getCheckIn).reversed());
        return responses;
    }

    @Override
    public BookingResponse updateStatus(long bookingId, String status) {
        Booking booking = bookingDomainRepository.getById(bookingId);
        if (booking == null) {
            throw new BusinessException(ErrorCode.BOOKING_NOT_FOUND);
        }
        booking.setStatus(BookingStatus.valueOf(status));
        bookingDomainRepository.update(booking);
        return getBookingResponse(booking);
    }

    private BookingResponse getBookingResponse(Booking booking) {
        BookingResponse response = bookingMapper.toDTO(booking);

        Payment payment = booking.getPayment();
        PaymentResponse paymentResponse = payment != null ? paymentMapper.toDTO(payment) : null;
        response.setPayment(paymentResponse);

        List<String> roomNames = new ArrayList<>();
        booking.getRooms().forEach(room -> roomNames.add(room.getName()));
        response.setRoomNames(roomNames);
        return response;
    }

    private String formatCurrency(long value) {
        return StringUtil.formatCurrency(value);
    }

    private Discount getMonthlyDiscount(Room room) {
        return room.getDiscounts().stream()
                .filter(discount -> discount.getType().equals(DiscountType.MONTHLY))
                .findFirst()
                .orElse(null);
    }

    private Discount getCustomDiscount(Room room, LocalDate checkIn, LocalDate checkOut) {
        return room.getDiscounts().stream()
                .filter(discount -> discount.getStartDate().isBefore(checkOut.atStartOfDay())
                        && discount.getEndDate().isAfter(checkIn.atStartOfDay().minusDays(1)))
                .findFirst()
                .orElse(null);
    }

    private boolean isDayInPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
