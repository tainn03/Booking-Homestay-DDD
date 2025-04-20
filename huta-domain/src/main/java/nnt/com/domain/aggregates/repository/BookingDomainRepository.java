package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingDomainRepository extends BaseBehaviors<Booking, Long> {

    List<Booking> getByStatus(BookingStatus bookingStatus);

    Booking getByCode(String orderInfo);

    List<Booking> getByHomestayId(long homestayId);

    List<Booking> getBookingsWithinDateRange(List<Long> roomIds, LocalDate startDate, LocalDate endDate);

    Object[] countNewAndReturningCustomers(List<Long> roomIds, LocalDateTime startOfYear, LocalDateTime startOfNextYear);
}
