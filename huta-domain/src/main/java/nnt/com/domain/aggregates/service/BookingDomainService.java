package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.time.LocalDate;

public interface BookingDomainService extends BaseBehaviors<Booking, Long> {
    void booking(BookingRequest request);

    boolean isRoomAvailable(long roomId, LocalDate checkIn, LocalDate checkOut);

    void cleanBookingExpiration();
}
