package nnt.com.application.service.booking;

import nnt.com.domain.aggregates.model.dto.request.BookingRequest;

public interface BookingAppService {
    void booking(BookingRequest request);
}
