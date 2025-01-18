package nnt.com.controller.http;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.model.dto.request.ChangePasswordRequest;
import nnt.com.application.model.dto.request.LoginRequest;
import nnt.com.application.service.authentication.AuthenticationAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.response.ApiResponse;
import nnt.com.domain.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationAppService authenticationAppService;
    ResponseFactory responseFactory;

    @PostMapping("/public/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseFactory.create(authenticationAppService.register(request)));
    }

    @PostMapping("/public/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(responseFactory.create(authenticationAppService.login(request)));
    }

    @PostMapping("/public/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@RequestParam String token, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseFactory.create(authenticationAppService.refreshToken(token, response)));
    }

    @PostMapping("/pwd")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authenticationAppService.changePassword(request);
        return ResponseEntity.ok(responseFactory.create(ErrorCode.SUCCESS));
    }
}
