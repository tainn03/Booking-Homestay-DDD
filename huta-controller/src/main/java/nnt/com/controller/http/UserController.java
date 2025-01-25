package nnt.com.controller.http;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.user.UserAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.response.ApiResponse;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/users")
public class UserController {
    UserAppService userAppService;
    ResponseFactory responseFactory;

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> getProfile() {
        UserResponse userResponse = userAppService.getProfile();
        return ResponseEntity.ok(responseFactory.create(userResponse));
    }
}
