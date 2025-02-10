package nnt.com.application.service.homestay.cache;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.request.RatingRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.dto.response.ImageResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.entity.Image;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.model.mapper.HomestayMapper;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.ImageDomainService;
import nnt.com.domain.aggregates.service.RecommendationService;
import nnt.com.domain.aggregates.service.UserDomainService;
import nnt.com.domain.shared.model.vo.LockKey;
import nnt.com.domain.shared.model.vo.RedisKey;
import nnt.com.infrastructure.cache.local.LocalCache;
import nnt.com.infrastructure.cache.redis.RedisCache;
import nnt.com.infrastructure.distributed.redisson.RedisDistributedLocker;
import nnt.com.infrastructure.distributed.redisson.RedisDistributedService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class HomestayAppServiceCache {
    HomestayDomainService homestayDomainService;
    RecommendationService recommendationService;
    ImageDomainService imageDomainService;
    UserDomainService userDomainService;

    RedisDistributedService distributedCache;
    RedisCache redisCache;
    LocalCache<HomestayResponse> localCache;
    HomestayMapper homestayMapper;

    public Page<HomestayResponse> getAll(int page, int size, String sortBy, String direction) {
        return homestayDomainService.getAllHomestay(page, size, sortBy, direction);
    }

    public HomestayResponse getHomestayById(Long homestayId) {
        HomestayResponse response = getHomestayFromLocalCache(homestayId);
        if (response != null) {
            return response;
        }

        String cacheKey = RedisKey.HOMESTAY.getKey() + homestayId;
        response = getHomestayFromGlobalCache(homestayId, cacheKey);
        if (response != null) {
            return response;
        }

        return getHomestayFromDatabaseWithLock(homestayId, cacheKey);
    }

    public HomestayResponse save(HomestayRequest request) {
        return homestayDomainService.save(request);
    }

    public void deleteById(Long homestayId) {
        imageDomainService.deleteFiles(homestayDomainService.getById(homestayId).getImages()
                .stream().map(Image::getUrl).toList());
        homestayDomainService.delete(homestayId);
        deleteCache(homestayId);
    }

    public HomestayResponse update(Long homestayId, HomestayRequest request) {
        deleteCache(homestayId);
        return homestayDomainService.updateHomestay(homestayId, request);
    }

    private HomestayResponse getHomestayFromLocalCache(Long homestayId) {
        HomestayResponse response = localCache.getIfPresent(homestayId);
        if (response != null) {
            log.info("GET HOMESTAY {} FROM LOCAL CACHE", homestayId);
        }
        return response;
    }

    private HomestayResponse getHomestayFromGlobalCache(Long homestayId, String cacheKey) {
        HomestayResponse response = redisCache.getObject(cacheKey, HomestayResponse.class);
        if (response != null) {
            log.info("GET HOMESTAY {} FROM DISTRIBUTED CACHE", homestayId);
            localCache.put(homestayId, response);
        }
        return response;
    }

    private HomestayResponse getHomestayFromDatabase(Long homestayId) {
        HomestayResponse response = homestayDomainService.getHomestayById(homestayId);
        log.info("GET HOMESTAY {} FROM DATABASE", homestayId);
        return response;
    }

    private HomestayResponse getHomestayFromDatabaseWithLock(Long homestayId, String cacheKey) {
        RedisDistributedLocker locker = distributedCache.getDistributedLocker(LockKey.HOMESTAY.getKey() + homestayId);
        try {
            boolean isLocked = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLocked) {
                return null;
            }

            HomestayResponse response = getHomestayFromGlobalCache(homestayId, cacheKey);
            if (response != null) {
                return response;
            }

            HomestayResponse homestayResponse = getHomestayFromDatabase(homestayId);
            redisCache.setObject(cacheKey, homestayResponse);
            localCache.put(homestayId, homestayResponse);
            return homestayResponse;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    private void deleteCache(Long homestayId) {
        localCache.invalidate(homestayId);
        redisCache.delete(RedisKey.HOMESTAY.getKey() + homestayId);
    }

    public List<ImageResponse> getHomestayImages(Long homestayId) {
        return convertImages(homestayDomainService.getById(homestayId).getImages());
    }

    public List<ImageResponse> uploadHomestayImage(Long homestayId, String type, List<MultipartFile> files) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        imageDomainService.uploadFiles(files).stream()
                .map(url -> Image.builder()
                        .url(url)
                        .type(type)
                        .homestay(homestay)
                        .build())
                .forEach(imageDomainService::save);
        return convertImages(homestay.getImages());
    }

    private List<ImageResponse> convertImages(List<Image> images) {
        return images.stream()
                .map(image -> ImageResponse.builder()
                        .id(image.getId())
                        .url(image.getUrl())
                        .type(image.getType())
                        .build())
                .toList();
    }

    public List<HomestayResponse> getHomestayByOwner() {
        return homestayDomainService.getByOwner(getCurrentUser().getId());
    }

    private User getCurrentUser() {
        return userDomainService.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public List<HomestayResponse> getWishlist() {
        return homestayDomainService.getWishlist(getCurrentUser());
    }

    public HomestayResponse ratingHomestay(Long homestayId, RatingRequest request) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        User user = getCurrentUser();
        deleteCache(homestayId);
        return homestayDomainService.ratingHomestay(homestay, user, request);
    }

    public List<RatingRequest> getRating(Long homestayId) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        return homestay.getReviews().stream()
                .map(review -> RatingRequest.builder()
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .createdAt(review.getCreatedAt())
                        .build())
                .toList();
    }

    public List<ImageResponse> uploadHomestayImageByUrl(Long homestayId, String type, List<String> urls) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        urls.stream()
                .map(url -> Image.builder()
                        .url(url)
                        .type(type)
                        .homestay(homestay)
                        .build())
                .forEach(imageDomainService::save);
        return convertImages(homestay.getImages());
    }

    public List<HomestayResponse> recommendHomestay() {
        return recommendationService.recommendHomestaysForUser(getCurrentUser().getId());
    }
}
