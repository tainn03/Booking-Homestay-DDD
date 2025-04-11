package nnt.com.application.service.user;

import nnt.com.domain.aggregates.model.dto.request.SubscriptionRequest;
import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.AnalysisSubscriptionResponse;
import nnt.com.domain.aggregates.model.dto.response.SubscriptionsResponse;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.aggregates.model.entity.Subscription;
import nnt.com.domain.aggregates.model.entity.UserSubscription;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserAppService {
    UserResponse getProfile();

    void updateAvatar(MultipartFile file);

    UserResponse updateProfile(UserUpdateRequest request);

    void likeHomestay(Long homestayId);

    boolean checkLikedHomestay(Long homestayId);

    UserResponse getProfileById(Long userId);

    boolean isCanCreateHomestay();

    Subscription createSubscription(SubscriptionRequest request);

    List<Subscription> getSubscriptions();

    Subscription updateSubscription(Long subscriptionId, SubscriptionRequest request);

    void deleteSubscription(Long subscriptionId);

    UserSubscription subscribe(Long subscriptionId);

    List<SubscriptionsResponse> getMySubscriptions();

    List<AnalysisSubscriptionResponse> getAnalysisSubscriptions(int year);

    List<UserResponse> getAllUsers();

    void blockUser(Long userId);

    void unblockUser(Long userId);
}
