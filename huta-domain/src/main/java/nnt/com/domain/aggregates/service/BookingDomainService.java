package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.aggregates.model.dto.response.BookingResponse;
import nnt.com.domain.aggregates.model.dto.response.PriceResponse;
import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.time.LocalDate;
import java.util.List;

public interface BookingDomainService extends BaseBehaviors<Booking, Long> {
    void booking(BookingRequest request, String code);

    boolean isRoomAvailable(long roomId, LocalDate checkIn, LocalDate checkOut);

    void cleanBookingExpiration();

    PriceResponse calculatePrice(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomIds);

    Booking getByCode(String orderInfo);

    BookingResponse getBookingByCode(String code);

    List<BookingResponse> getBookingsByHomestay(long homestayId);

    BookingResponse updateStatus(long bookingId, String status);

    List<BookingResponse> getMyBookings();
}
