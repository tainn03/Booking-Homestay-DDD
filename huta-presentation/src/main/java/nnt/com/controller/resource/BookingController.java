package nnt.com.controller.resource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.booking.BookingAppService;
import nnt.com.controller.aop.annotation.UserActionLog;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.shared.model.vo.UserAction;
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
    @UserActionLog(action = UserAction.BOOKING)
    public ApiResponse createBooking(@Valid @RequestBody BookingRequest bookingRequest) {
        return responseFactory.create(bookingAppService.booking(bookingRequest));
    }

    @GetMapping("/price")
    public ApiResponse calculatePrice(@RequestParam long homestayId,
                                      @RequestParam LocalDate checkIn,
                                      @RequestParam LocalDate checkOut,
                                      @RequestParam int guests,
                                      @RequestParam long roomId) {
        return responseFactory.create(bookingAppService.calculatePrice(homestayId, checkIn, checkOut, guests, roomId));
    }

    @GetMapping("/{code}")
    public ApiResponse getBooking(@PathVariable String code) {
        return responseFactory.create(bookingAppService.getBookingByCode(code));
    }

    @GetMapping("/homestays/{homestayId}")
    public ApiResponse getBookingsByHomestay(@PathVariable long homestayId) {
        return responseFactory.create(bookingAppService.getBookingsByHomestay(homestayId));
    }

    @DeleteMapping("/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ApiResponse deleteBooking(@PathVariable long bookingId) {
        bookingAppService.deleteBooking(bookingId);
        return responseFactory.create("Booking deleted successfully");
    }

    @PatchMapping("/{bookingId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ApiResponse updateBookingStatus(@PathVariable long bookingId, @RequestParam String status) {
        return responseFactory.create(bookingAppService.updateBookingStatus(bookingId, status));
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ApiResponse getMyBookings() {
        return responseFactory.create(bookingAppService.getMyBookings());
    }
}
