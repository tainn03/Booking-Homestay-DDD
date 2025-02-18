package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.aggregates.model.entity.*;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import nnt.com.domain.aggregates.model.vo.DiscountType;
import nnt.com.domain.aggregates.repository.BookingDomainRepository;
import nnt.com.domain.aggregates.repository.HomestayDomainRepository;
import nnt.com.domain.aggregates.repository.RoomAvailableDomainRepository;
import nnt.com.domain.aggregates.repository.UserDomainRepository;
import nnt.com.domain.aggregates.service.BookingDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public void booking(BookingRequest request) {
        Homestay homestay = homestayDomainRepository.getById(request.getHomestayId());
        int guests = request.getAdult() + request.getChildren() + request.getInfant() / 2;

        Room selectedRoom = getRoomAvailable(homestay, request.getRoomId(), guests, request.getCheckIn(), request.getCheckOut());

        saveBookingToDB(request, selectedRoom);
        saveAvailableRoomToDB(selectedRoom, request.getCheckIn(), request.getCheckOut());
    }

    private void saveBookingToDB(BookingRequest request, Room selectedRoom) {
        int nights = (int) ChronoUnit.DAYS.between(request.getCheckIn(), request.getCheckOut());
        Booking savedBooking = bookingDomainRepository.save(Booking.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .code("BK-" + String.valueOf(System.currentTimeMillis()).substring(0, 6))
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
        log.info("SAVE BOOKING: {}", savedBooking.getId());
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
                booking.setStatus(BookingStatus.CANCELLED);
                bookingDomainRepository.update(booking);
            }
        });
    }

    private long calculateTotalCost(List<Room> selectedRooms, LocalDate checkIn, LocalDate checkOut) {
        long totalCost = 0;
        int nights = (int) ChronoUnit.DAYS.between(checkIn, checkOut);
        for (Room room : selectedRooms) {
            Discount monthlyDiscount = getMonthlyDiscount(room);
            Discount customDiscount = getCustomDiscount(room, checkIn, checkOut);

            // map through each day to calculate total cost
            for (LocalDate i = checkIn; i.isBefore(checkOut); i = i.plusDays(1)) {
                boolean isWeekday = i.getDayOfWeek().getValue() < 6;
                totalCost += isWeekday ? room.getDailyPrice() : room.getWeekendPrice();
                log.info("CURRENT COST IN DAY {} FOR ROOM {}: {}", i.format(DateTimeFormatter.ISO_DATE), room.getId(), totalCost);

                // check custom discount and apply
                if (customDiscount != null && isDayInPeriod(i, customDiscount.getStartDate().toLocalDate(), customDiscount.getEndDate().toLocalDate())) {
                    totalCost = (long) (totalCost * (1 - customDiscount.getValue() / 100));
                    log.info("APPLY DAILY DISCOUNT: {}", customDiscount.getValue());
                    log.info("CURRENT COST IN DAY {} FOR ROOM {}: {}", i.format(DateTimeFormatter.ISO_DATE), room.getId(), totalCost);
                }
            }

            // check monthly discount and apply
            if (nights > 30 && monthlyDiscount != null) {
                log.info("APPLY MONTHLY DISCOUNT: {}", monthlyDiscount.getValue());
                totalCost = (long) (totalCost * (1 - monthlyDiscount.getValue() / 100));
            }
            log.info("TOTAL COST FOR ROOM {}: {}", room.getId(), totalCost);
        }
        return totalCost;
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
