package nnt.com.controller.resource;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.tripAI.TripDocumentAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import nnt.com.domain.aggregates.model.document.TripDocument;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/trips")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TripController {
    ResponseFactory responseFactory;
    TripDocumentAppService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ApiResponse getAllTrips() {
        return responseFactory.create(service.getAll());
    }

    @GetMapping("/{id}")
    public ApiResponse getTripById(@PathVariable String id) {
        return responseFactory.create(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ApiResponse createTrip(@RequestBody TripDocument trip) {
        return responseFactory.create(service.save(trip));
    }

    @DeleteMapping("/{id}")
    public ApiResponse deleteTrip(@PathVariable String id) {
        service.delete(id);
        return responseFactory.create("Trip deleted successfully");
    }
}
