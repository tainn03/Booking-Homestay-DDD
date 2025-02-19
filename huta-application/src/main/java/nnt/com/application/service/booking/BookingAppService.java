package nnt.com.application.service.booking;

import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.aggregates.model.dto.response.PriceResponse;

import java.time.LocalDate;

public interface BookingAppService {
    void booking(BookingRequest request);

    PriceResponse calculatePrice(long homestayId, LocalDate checkIn, LocalDate checkOut, int guests, long roomId);
}
