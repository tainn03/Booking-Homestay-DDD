package nnt.com.controller.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.booking.BookingAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
        return responseFactory.create(bookingAppService.booking(bookingRequest));
    }

    @GetMapping("/homestays/{homestayId}")
    public ApiResponse calculatePrice(@PathVariable long homestayId,
                                      @RequestParam LocalDate checkIn,
                                      @RequestParam LocalDate checkOut,
                                      @RequestParam int guests,
                                      @RequestParam long roomId) {
        return responseFactory.create(bookingAppService.calculatePrice(homestayId, checkIn, checkOut, guests, roomId));
    }
}
