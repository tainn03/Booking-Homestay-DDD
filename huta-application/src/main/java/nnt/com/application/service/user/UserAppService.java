package nnt.com.application.service.user;

import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserAppService {
    UserResponse getProfile();

    void updateAvatar(MultipartFile file);

    UserResponse updateProfile(UserUpdateRequest request);

    void likeHomestay(Long homestayId);

}
