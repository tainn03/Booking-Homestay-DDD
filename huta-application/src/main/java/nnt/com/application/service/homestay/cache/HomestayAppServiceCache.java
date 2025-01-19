package nnt.com.application.service.homestay.cache;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.mapper.HomestayMapper;
import nnt.com.domain.aggregates.service.DistrictDomainService;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.TypeHomestayDomainService;
import nnt.com.domain.aggregates.service.UserDomainService;
import nnt.com.domain.shared.model.enums.LockKey;
import nnt.com.domain.shared.model.enums.RedisKey;
import nnt.com.infrastructure.cache.local.LocalCache;
import nnt.com.infrastructure.cache.redis.RedisCache;
import nnt.com.infrastructure.distributed.redisson.RedisDistributedLocker;
import nnt.com.infrastructure.distributed.redisson.RedisDistributedService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class HomestayAppServiceCache {
    HomestayDomainService homestayDomainService;
    DistrictDomainService districtDomainService;
    TypeHomestayDomainService typeHomestayDomainService;
    UserDomainService userDomainService;
    HomestayMapper homestayMapper;

    RedisDistributedService distributedCache;
    RedisCache redisCache;
    LocalCache<HomestayResponse> localCache;

    public Page<HomestayResponse> getAll(int page, int size, String sortBy, String direction) {
        Page<Homestay> homestays = homestayDomainService.getAll(page, size, sortBy, direction);
        return homestays.map(homestayMapper::toDTO);
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
        Homestay homestay = homestayMapper.toEntity(request);
        homestay.setDistrict(districtDomainService.getByName(request.getDistrict(), request.getCity()));
        homestay.setTypeHomestay(typeHomestayDomainService.getById(request.getTypeHomestay()));
        homestay.setOwner(userDomainService.getByEmail(request.getEmailOwner()));
        return homestayMapper.toDTO(homestayDomainService.save(homestay));
    }

    public void deleteById(Long homestayId) {
        homestayDomainService.delete(homestayId);
        localCache.invalidate(homestayId);
        redisCache.delete(RedisKey.HOMESTAY.getKey() + homestayId);
    }

    public HomestayResponse update(Long homestayId, HomestayRequest request) {
        Homestay homestay = homestayDomainService.getById(homestayId);
        Homestay updatedHomestay = homestayMapper.updateEntity(homestay, request);
        updatedHomestay = homestayDomainService.update(updatedHomestay);
        return updateWithCache(updatedHomestay);
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
        Homestay homestay = homestayDomainService.getById(homestayId);
        log.info("GET HOMESTAY {} FROM DATABASE", homestayId);
        return homestayMapper.toDTO(homestay);
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

    private HomestayResponse updateWithCache(Homestay homestay) {
        HomestayResponse response = homestayMapper.toDTO(homestay);
        if (homestay != null) {
            redisCache.setObject(RedisKey.HOMESTAY.getKey() + homestay.getId(), response);
            localCache.put(homestay.getId(), response);
        }
        return response;
    }

}
