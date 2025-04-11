package nnt.com.controller.resource;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.user.UserAppService;
import nnt.com.controller.model.builder.ResponseFactory;
import nnt.com.controller.model.vo.ApiResponse;
import nnt.com.domain.aggregates.model.dto.request.SubscriptionRequest;
import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.http.HttpStatus;
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

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getUsers() {
        return ResponseEntity.ok(responseFactory.create(userAppService.getAllUsers()));
    }

    @PatchMapping("/block/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> blockUser(@PathVariable Long userId) {
        userAppService.blockUser(userId);
        return ResponseEntity.ok(responseFactory.create("Khóa tài khoản thành công"));
    }

    @PatchMapping("/unblock/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> unblockUser(@PathVariable Long userId) {
        userAppService.unblockUser(userId);
        return ResponseEntity.ok(responseFactory.create("Mở khóa tài khoản thành công"));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> getProfile() {
        UserResponse userResponse = userAppService.getProfile();
        return ResponseEntity.ok(responseFactory.create(userResponse));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ApiResponse> getProfileById(@PathVariable Long userId) {
        UserResponse userResponse = userAppService.getProfileById(userId);
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

    @GetMapping("/homestay/{homestayId}/like")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> checkLikedHomestay(@PathVariable Long homestayId) {
        return ResponseEntity.ok(responseFactory.create(userAppService.checkLikedHomestay(homestayId)));
    }

    @GetMapping("/homestays/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> isCanCreateHomestay() {
        return ResponseEntity.ok(responseFactory.create(userAppService.isCanCreateHomestay()));
    }

    @PostMapping("/subscriptions")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> createSubscription(@RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(responseFactory.create(userAppService.createSubscription(request)));
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<ApiResponse> getSubscriptions() {
        return ResponseEntity.ok(responseFactory.create(userAppService.getSubscriptions()));
    }

    @PutMapping("/subscriptions/{subscriptionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> updateSubscription(@PathVariable Long subscriptionId, @RequestBody SubscriptionRequest request) {
        return ResponseEntity.ok(responseFactory.create(userAppService.updateSubscription(subscriptionId, request)));
    }

    @DeleteMapping("/subscriptions/{subscriptionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse deleteSubscription(@PathVariable Long subscriptionId) {
        userAppService.deleteSubscription(subscriptionId);
        return responseFactory.create("Xóa thành công");
    }

    @PostMapping("/subscriptions/{subscriptionId}/subscribe")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> subscribe(@PathVariable Long subscriptionId) {
        return ResponseEntity.ok(responseFactory.create(userAppService.subscribe(subscriptionId)));
    }

    @GetMapping("/subscriptions/mine")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'LANDLORD')")
    public ResponseEntity<ApiResponse> getMySubscriptions() {
        return ResponseEntity.ok(responseFactory.create(userAppService.getMySubscriptions()));
    }

    @GetMapping("/subscriptions/analysis")
    public ResponseEntity<ApiResponse> getAnalysisSubscriptions(@RequestParam int year) {
        return ResponseEntity.ok(responseFactory.create(userAppService.getAnalysisSubscriptions(year)));
    }
}
