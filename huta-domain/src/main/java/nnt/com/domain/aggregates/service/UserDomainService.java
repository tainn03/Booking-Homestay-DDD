package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.model.entity.UserSubscription;
import nnt.com.domain.shared.behaviors.BaseBehaviors;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserDomainService extends BaseBehaviors<User, Long> {
    User getByEmail(String emailOwner);

    UserResponse getProfile();

    void updateAvatar(MultipartFile file);

    UserResponse updateProfile(UserUpdateRequest request);

    void likeHomestay(User user, Homestay homestay);

    boolean checkLikedHomestay(User user, long homestayId);

    UserResponse getProfileById(Long userId);

    boolean isCanCreateHomestay();

    List<UserSubscription> getMySubscriptions();

    List<UserResponse> getAllUsers();

    void blockUser(Long userId);

    void unblockUser(Long userId);
}
