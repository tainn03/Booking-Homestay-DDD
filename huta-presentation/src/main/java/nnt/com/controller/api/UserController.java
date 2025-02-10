package nnt.com.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.user.UserAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody UserUpdateRequest request) {
        UserResponse userResponse = userAppService.updateProfile(request);
        return ResponseEntity.ok(responseFactory.create(userResponse));
    }

    @PatchMapping("/avatar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> updateAvatar(@RequestBody MultipartFile file) {
        userAppService.updateAvatar(file);
        return ResponseEntity.ok(responseFactory.create("Cập nhật ảnh đại diện thành công"));
    }

    @PatchMapping("/like")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> likeHomestay(@RequestParam Long homestayId) {
        userAppService.likeHomestay(homestayId);
        return ResponseEntity.ok(responseFactory.create(ErrorCode.SUCCESS));
    }
}
