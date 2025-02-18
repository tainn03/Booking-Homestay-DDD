package nnt.com.controller.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.booking.BookingAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BookingController {
    ResponseFactory responseFactory;
    BookingAppService bookingAppService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ApiResponse createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        bookingAppService.booking(bookingRequest);
        return responseFactory.create("Booking created successfully");
    }
}
