package nnt.com.controller.http;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.authentication.AuthenticationAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.response.ApiResponse;
import nnt.com.domain.aggregates.model.dto.request.ChangePasswordRequest;
import nnt.com.domain.aggregates.model.dto.request.LoginRequest;
import nnt.com.domain.aggregates.model.dto.request.RegisterRequest;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationAppService authenticationAppService;
    ResponseFactory responseFactory;

    @PostMapping("/public/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
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

    @GetMapping("/public/google")
    public ResponseEntity<ApiResponse> loginGoogleAuth(HttpServletResponse response) throws IOException {
        authenticationAppService.loginGoogleAuth(response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/grantcode")
    public void grantCode(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        // Gọi hàm lấy access token từ Google
        authenticationAppService.getOauthAccessTokenGoogle(code, response);
    }

    @PostMapping("/forgot-password")
    public ApiResponse forgotPassword(@RequestParam String email) {
        authenticationAppService.forgotPassword(email);
        return responseFactory.create(ErrorCode.SUCCESS);
    }

    @PostMapping("/register-landlord")
    public ApiResponse registerLandlord(@RequestParam String email) {
//        return ApiResponse.<String>builder()
//                .result(service.registerLandlord(email))
//                .build();
        return null;
    }

    @GetMapping("/confirm-landlord")
    public void confirmLandlord(@RequestParam String token, HttpServletResponse response) throws IOException {
//        service.confirmLandlord(token, response);
    }
}
