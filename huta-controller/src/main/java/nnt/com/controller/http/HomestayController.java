package nnt.com.controller.http;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.homestay.HomestayAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.response.ApiResponse;
import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/homestays")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomestayController {
    HomestayAppService homestayAppService;
    ResponseFactory responseFactory;

    @GetMapping
    @RateLimiter(name = "backendB", fallbackMethod = "fallbackMethod")
    public ResponseEntity<ApiResponse> getAllHomestay(@RequestParam(required = false, defaultValue = "0") int page,
                                                      @RequestParam(required = false, defaultValue = "10") int size,
                                                      @RequestParam(required = false, defaultValue = "id") String sortBy,
                                                      @RequestParam(required = false, defaultValue = "asc") String direction
    ) {
        Page<HomestayResponse> data = homestayAppService.getAll(page, size, sortBy, direction);
        return ResponseEntity.ok(responseFactory.create(data));
    }

    public ResponseEntity<ApiResponse> fallbackMethod(Throwable throwable) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(responseFactory.create(ErrorCode.TOO_MANY_REQUESTS));
    }

    @GetMapping("/{homestayId}")
    public ResponseEntity<ApiResponse> getHomestayById(@PathVariable Long homestayId) {
        HomestayResponse data = homestayAppService.getHomestayById(homestayId);
        return ResponseEntity.ok(responseFactory.create(data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> saveHomestay(@Valid @RequestBody HomestayRequest homestay) {
        HomestayResponse response = homestayAppService.save(homestay);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseFactory.create(HttpStatus.CREATED, response));
    }

    @DeleteMapping("/{homestayId}")
    public ResponseEntity<ApiResponse> deleteHomestay(@PathVariable Long homestayId) {
        homestayAppService.deleteById(homestayId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(responseFactory.create(ErrorCode.SUCCESS));
    }

    @PutMapping("/{homestayId}")
    public ResponseEntity<ApiResponse> updateHomestay(@PathVariable Long homestayId,
                                                      @Valid @RequestBody HomestayRequest request) {
        HomestayResponse response = homestayAppService.update(homestayId, request);
        return ResponseEntity.ok(responseFactory.create(response));
    }
}
