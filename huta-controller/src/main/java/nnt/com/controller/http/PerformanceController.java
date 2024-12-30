package nnt.com.controller.http;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.controller.model.builder.impl.ResponseFactoryImpl;
import nnt.com.controller.model.response.ApiResponse;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/perf")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PerformanceController {
    ResponseFactoryImpl responseFactory;

    @GetMapping("/circuit/breaker")
    @CircuitBreaker(name = "booking", fallbackMethod = "fallbackCircuitBreaker")
    public ResponseEntity<ApiResponse> circuitBreaker() {
        // Simulating a random failure
        if (Math.random() > 0.5) {
            throw new BusinessException(ErrorCode.SERVICE_UNAVAILABLE);
        }
        return ResponseEntity.ok(responseFactory.create(ErrorCode.SUCCESS));
    }

    public ResponseEntity<ApiResponse> fallbackCircuitBreaker(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(responseFactory.create(ErrorCode.SERVICE_UNAVAILABLE));
    }
}
