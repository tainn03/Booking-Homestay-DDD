package nnt.com.application.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.user.UserAppService;
import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.UserDomainService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserAppServiceImpl implements UserAppService {
    UserDomainService userDomainService;
    HomestayDomainService homestayDomainService;

    @Override
    public UserResponse getProfile() {
        return userDomainService.getProfile();
    }

    @Override
    public void updateAvatar(MultipartFile file) {
        userDomainService.updateAvatar(file);
    }

    @Override
    public UserResponse updateProfile(UserUpdateRequest request) {
        return userDomainService.updateProfile(request);
    }

    @Override
    public void likeHomestay(Long homestayId) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        User user = userDomainService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        userDomainService.likeHomestay(user, homestay);
    }
}
