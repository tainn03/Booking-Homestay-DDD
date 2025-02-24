package nnt.com.application.service.booking;

import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.aggregates.model.dto.response.BookingResponse;
import nnt.com.domain.aggregates.model.dto.response.PriceResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookingAppService {
    String booking(BookingRequest request);

    PriceResponse calculatePrice(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomId);

    BookingResponse getBookingByCode(String code);

    List<BookingResponse> getBookingsByHomestay(long homestayId);
}
