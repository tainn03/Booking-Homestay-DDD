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
import nnt.com.domain.aggregates.model.mapper.UserMapper;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import nnt.com.domain.aggregates.model.vo.DiscountType;
import nnt.com.domain.aggregates.repository.*;
import nnt.com.domain.aggregates.service.BookingDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.domain.shared.utils.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
    UserMapper userMapper;

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
        checkUserInfo(request);

        Homestay homestay = homestayDomainRepository.getById(request.getHomestayId());
        int guests = request.getAdult() + request.getChildren() + request.getInfant() / 2;

        List<Room> selectedRooms = getRoomAvailable(homestay, request.getRoomIds(), guests, request.getCheckIn(), request.getCheckOut());

        saveBookingToDB(request, selectedRooms, code);
        saveAvailableRoomToDB(selectedRooms, request.getCheckIn(), request.getCheckOut());
    }

    @Async
    protected void checkUserInfo(BookingRequest request) {
        User user = userDomainRepository.getByEmail(request.getEmail());
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            user.setPhone(request.getPhone());
            userDomainRepository.update(user);
        }
    }

    private void saveBookingToDB(BookingRequest request, List<Room> selectedRooms, String code) {
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
                .rooms(selectedRooms)
                .build());
        log.info("SAVE BOOKING: {}", savedBooking.getCode());
    }

    private void saveAvailableRoomToDB(List<Room> room, LocalDate checkIn, LocalDate checkOut) {
        for (LocalDate i = checkIn; i.isBefore(checkOut); i = i.plusDays(1)) {
            for (Room roomAvailable : room) {
                RoomAvailable available = roomAvailableDomainRepository.getById(roomAvailable.getId());
                if (available == null) {
                    available = RoomAvailable.builder()
                            .room(roomAvailable)
                            .date(i)
                            .available(0)
                            .build();
                } else {
                    available.setAvailable(0);
                }
                roomAvailableDomainRepository.save(available);
            }
        }
    }

    private List<Room> getRoomAvailable(Homestay homestay, List<Long> roomIds, int guests, LocalDate checkIn, LocalDate checkOut) {
        return getSelectedRooms(homestay, roomIds);
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

    private List<Room> getSelectedRooms(Homestay homestay, List<Long> roomIds) {
        log.info("GET SELECTED ROOM: {}", roomIds);
        return homestay.getRooms().stream()
                .filter(room -> roomIds.contains(room.getId()))
                .collect(Collectors.toList());
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
    public PriceResponse calculatePriceInManyRooms(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomId) {
        Set<Room> bookedRooms = new HashSet<>();
        int remainingGuests = guests;

        // Get the initially selected room
        Room room = roomDomainRepository.getById(roomId);
        if (!isRoomAvailable(roomId, checkIn, checkOut)) {
            throw new BusinessException(ErrorCode.ROOM_NOT_AVAILABLE);
        }
        bookedRooms.add(room);
        remainingGuests -= room.getSize();

        // Add additional rooms if needed
        List<Room> additionalRoom = homestayDomainRepository.getById(homestayId).getRooms().stream()
                .filter(r -> r.getId() != roomId)
                .filter(r -> isRoomAvailable(r.getId(), checkIn, checkOut))
                .sorted(Comparator.comparing(Room::getSize))
                .collect(Collectors.toList());
        while (remainingGuests > 0) {
            Room nextRoom;
            if (remainingGuests > additionalRoom.getLast().getSize()) {
                nextRoom = additionalRoom.getLast();
            } else {
                int finalRemainingGuests = remainingGuests;
                nextRoom = additionalRoom.stream()
                        .filter(r -> r.getSize() >= finalRemainingGuests)
                        .min(Comparator.comparing(Room::getSize))
                        .orElseThrow(() -> new BusinessException(ErrorCode.NO_ROOM_AVAILABLE));
            }

            bookedRooms.add(nextRoom);
            remainingGuests -= nextRoom.getSize();
            additionalRoom.remove(nextRoom);
        }

        // Calculate price for all booked rooms
        List<PriceResponse> priceResponses = bookedRooms.stream()
                .map(room1 -> calculatePrice(homestayId, checkIn, checkOut, guests, room1.getId()))
                .toList();
        long totalOriginalCost = priceResponses.stream()
                .mapToLong(priceResponse -> StringUtil.parseCurrency(priceResponse.getOriginalCost()))
                .sum();
        long totalDiscountValue = priceResponses.stream()
                .mapToLong(priceResponse -> StringUtil.parseCurrency(priceResponse.getDiscountValue()))
                .sum();
        long totalCost = priceResponses.stream()
                .mapToLong(priceResponse -> StringUtil.parseCurrency(priceResponse.getTotalCost()))
                .sum();
        int totalDailyDays = priceResponses.stream()
                .mapToInt(PriceResponse::getDailyDays)
                .sum();
        int totalWeekendDays = priceResponses.stream()
                .mapToInt(PriceResponse::getWeekendDays)
                .sum();
        long dailyPrice = room.getDailyPrice();
        long weekendPrice = room.getWeekendPrice();
        return PriceResponse.builder()
                .suitableRoomIds(bookedRooms.stream().map(Room::getId).map(String::valueOf).toList())
                .originalCost(formatCurrency(totalOriginalCost))
                .discountValue(formatCurrency(totalDiscountValue))
                .totalCost(formatCurrency(totalCost))
                .dailyDays(totalDailyDays)
                .dailyPrice(formatCurrency(dailyPrice))
                .weekendDays(totalWeekendDays)
                .weekendPrice(formatCurrency(weekendPrice))
                .build();
    }

    @Override
    public PriceResponse calculatePrice(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomId) {
        int dailyDays = 0;
        int weekendDays = 0;
        long originalCost = 0;
        long discountValue = 0;

        // get room and discounts
        Room room = roomDomainRepository.getById(roomId);
        Discount monthlyDiscount = getMonthlyDiscount(room);
        Discount weeklyDiscount = getWeeklyDiscount(room);
        Discount customDiscount = getCustomDiscount(room, checkIn, checkOut);

        // map through each day to calculate price
        for (LocalDate i = checkIn; i.isBefore(checkOut); i = i.plusDays(1)) {
            boolean isWeekday = i.getDayOfWeek().getValue() < 6;
            dailyDays += isWeekday ? 1 : 0;
            weekendDays += isWeekday ? 0 : 1;
            long currentCost = isWeekday ? room.getDailyPrice() : room.getWeekendPrice();
            originalCost += currentCost;

            // check custom discount and apply
            if (customDiscount != null && isDayInPeriod(i, customDiscount.getStartDate(), customDiscount.getEndDate())) {
                discountValue += currentCost * customDiscount.getValue() / 100;
                log.info("APPLY DAILY DISCOUNT: {}", customDiscount.getValue());
            }
        }

        // check monthly discount and apply
        if (ChronoUnit.DAYS.between(checkIn, checkOut) >= 30 && monthlyDiscount != null) {
            log.info("APPLY MONTHLY DISCOUNT: {}", monthlyDiscount.getValue());
            discountValue += originalCost * monthlyDiscount.getValue() / 100;
        }

        // check weekly discount and apply
        if (ChronoUnit.DAYS.between(checkIn, checkOut) >= 7 && weeklyDiscount != null) {
            log.info("APPLY WEEKLY DISCOUNT: {}", weeklyDiscount.getValue());
            discountValue += originalCost * weeklyDiscount.getValue() / 100;
        }

        return PriceResponse.builder()
                .originalCost(formatCurrency(originalCost))
                .discountValue(formatCurrency(discountValue))
                .totalCost(formatCurrency(originalCost - discountValue))
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
        List<Booking> bookings = bookingDomainRepository.getByHomestayId(homestayId).stream()
                .filter(response -> !response.getStatus().equals(BookingStatus.EXPIRED))
                .sorted(Comparator.comparing(Booking::getCheckIn).reversed())
                .toList();
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

    @Override
    public List<BookingResponse> getMyBookings() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Booking> bookings = user.getBookings().stream()
                .filter(response -> !response.getStatus().equals(BookingStatus.EXPIRED))
                .sorted(Comparator.comparing(Booking::getCheckIn).reversed())
                .toList();
        List<BookingResponse> responses = new ArrayList<>();
        bookings.forEach(booking -> {
            BookingResponse response = getBookingResponse(booking);
            responses.add(response);
        });
        return responses;
    }

    @Override
    public List<Booking> getBookingsWithinDateRange(List<Long> roomIds, LocalDate startDate, LocalDate endDate) {
        return bookingDomainRepository.getBookingsWithinDateRange(roomIds, startDate, endDate);
    }

    @Override
    public Object[] countNewAndReturningCustomers(List<Long> roomIds, LocalDateTime startOfYear, LocalDateTime startOfNextYear) {
        return bookingDomainRepository.countNewAndReturningCustomers(roomIds, startOfYear, startOfNextYear);
    }

    private BookingResponse getBookingResponse(Booking booking) {
        BookingResponse response = bookingMapper.toDTO(booking);

        Payment payment = booking.getPayment();
        PaymentResponse paymentResponse = payment != null ? paymentMapper.toDTO(payment) : null;
        response.setPayment(paymentResponse);


        response.setRoomIds(booking.getRooms().stream().map(Room::getId).toList());
        response.setUser(userMapper.toDTO(booking.getUser()));

        response.setCheckIn(booking.getCheckIn().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        response.setCheckOut(booking.getCheckOut().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        response.setTotalCost(formatCurrency(booking.getTotalCost()));

        response.setHomestayName(homestayDomainRepository.getById(booking.getRooms().getFirst().getHomestay().getId()).getHomestayName());
        response.setRoomName(booking.getRooms().stream().map(Room::getName).collect(Collectors.joining(", ")));
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

    private Discount getWeeklyDiscount(Room room) {
        return room.getDiscounts().stream()
                .filter(discount -> discount.getType().equals(DiscountType.WEEKLY))
                .findFirst()
                .orElse(null);
    }

    private Discount getCustomDiscount(Room room, LocalDate checkIn, LocalDate checkOut) {
        return room.getDiscounts().stream()
                .filter(discount -> discount.getStartDate().isBefore(ChronoLocalDate.from(checkOut.atStartOfDay()))
                        && discount.getEndDate().isAfter(ChronoLocalDate.from(checkIn.atStartOfDay().minusDays(1))))
                .findFirst()
                .orElse(null);
    }

    private boolean isDayInPeriod(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
