package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.shared.behaviors.BaseBehaviors;
import org.springframework.web.multipart.MultipartFile;

public interface UserDomainService extends BaseBehaviors<User, Long> {
    User getByEmail(String emailOwner);

    UserResponse getProfile();

    void updateAvatar(MultipartFile file);

    UserResponse updateProfile(UserUpdateRequest request);
}
