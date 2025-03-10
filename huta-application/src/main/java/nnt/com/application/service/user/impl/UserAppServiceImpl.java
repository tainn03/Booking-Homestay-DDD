package nnt.com.application.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.homestay.cache.HomestayAppServiceCache;
import nnt.com.application.service.user.UserAppService;
import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.UserDomainService;
import nnt.com.infrastructure.cache.redis.RedisCache;
import nnt.com.infrastructure.distributed.redisson.BloomFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserAppServiceImpl implements UserAppService {
    private static final Logger log = LoggerFactory.getLogger(UserAppServiceImpl.class);
    UserDomainService userDomainService;
    HomestayDomainService homestayDomainService;
    HomestayAppServiceCache homestayAppServiceCache;
    RedisCache redisCache;
    BloomFilterService bloomFilterService;

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
        updateCache(user.getId() + ":like:" + homestayId);
    }

    @Override
    public boolean checkLikedHomestay(Long homestayId) {
        HomestayResponse homestay = homestayAppServiceCache.getHomestayById(homestayId);
        User user = userDomainService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        String key = user.getId() + ":like:" + homestayId;
        if (!checkLikedHomestayInBloomFilter(key)) {
            log.info("KEY {} NOT FOUND IN BLOOM FILTER", key);
            return false;
        }
        if (checkLikedHomestayInDB(user, homestay.getId())) {
            log.info("USER HAS LIKED HOMESTAY {}", homestayId);
            return true;
        }
        log.info("USER HAS NOT LIKED HOMESTAY {}", homestayId);
        return false;
    }

    @Override
    public UserResponse getProfileById(Long userId) {
        return userDomainService.getProfileById(userId);
    }

    @Override
    public boolean isCanCreateHomestay() {
        return userDomainService.isCanCreateHomestay();
    }

    private boolean checkLikedHomestayInBloomFilter(String key) {
        return bloomFilterService.mightContain(key);
    }

    private boolean checkLikedHomestayInDB(User user, long homestayId) {
        return userDomainService.checkLikedHomestay(user, homestayId);
    }

    private void updateCache(String key) {
        bloomFilterService.add(key);
    }
}
