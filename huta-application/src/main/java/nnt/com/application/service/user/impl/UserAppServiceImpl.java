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
import nnt.com.infrastructure.cache.redis.RedisCache;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserAppServiceImpl implements UserAppService {
    UserDomainService userDomainService;
    HomestayDomainService homestayDomainService;
    RedisCache redisCache;

    @Override
    public UserResponse getProfile() {
        UserResponse response = userDomainService.getProfile();
        redisCache.setObject(SecurityContextHolder.getContext().getAuthentication().getName() + ":profile", response, 30L, TimeUnit.MINUTES);
        return response;
    }

    @Override
    public void updateAvatar(MultipartFile file) {
        userDomainService.updateAvatar(file);
        redisCache.delete(SecurityContextHolder.getContext().getAuthentication().getName() + ":profile");
    }

    @Override
    public UserResponse updateProfile(UserUpdateRequest request) {
        UserResponse response = userDomainService.updateProfile(request);
        redisCache.setObject(SecurityContextHolder.getContext().getAuthentication().getName() + ":profile", response, 30L, TimeUnit.MINUTES);
        return response;
    }

    @Override
    public void likeHomestay(Long homestayId) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        User user = userDomainService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        userDomainService.likeHomestay(user, homestay);
    }
}
