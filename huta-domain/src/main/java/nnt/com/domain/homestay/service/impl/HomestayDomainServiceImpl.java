package nnt.com.domain.homestay.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.base.cache.local.LocalCache;
import nnt.com.domain.base.cache.redis.RedisCache;
import nnt.com.domain.base.distributed.RedisDistributedLocker;
import nnt.com.domain.base.distributed.RedisDistributedService;
import nnt.com.domain.base.exception.DomainDistributedLockException;
import nnt.com.domain.base.exception.DomainNotFoundException;
import nnt.com.domain.base.model.enums.LockKey;
import nnt.com.domain.base.model.enums.RedisKey;
import nnt.com.domain.homestay.model.entity.Homestay;
import nnt.com.domain.homestay.repository.HomestayDomainRepository;
import nnt.com.domain.homestay.service.HomestayDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class HomestayDomainServiceImpl implements HomestayDomainService {
    HomestayDomainRepository homestayDomainRepository;
    RedisDistributedService distributedCache;
    RedisCache redisCache;
    LocalCache<Homestay> localCache;

    @Override
    public Page<Homestay> findAll(int page, int size, String sortBy, String direction) {
        return homestayDomainRepository.findAll(page, size, sortBy, direction);
    }

    @Override
    public Homestay findById(Long homestayId) {
        Homestay homestay = getHomestayFromLocalCache(homestayId);
        if (homestay != null) {
            return homestay;
        }

        String cacheKey = RedisKey.HOMESTAY.getKey() + homestayId;
        homestay = getHomestayFromGlobalCache(homestayId, cacheKey);
        if (homestay != null) {
            return homestay;
        }

        return getHomestayFromDatabaseWithLock(homestayId, cacheKey);
    }

    @Override
    public Homestay save(Homestay homestay) {
        homestay = homestayDomainRepository.save(homestay);
        if (homestay != null) {
            String cacheKey = RedisKey.HOMESTAY.getKey() + homestay.getId();
            redisCache.setObject(cacheKey, homestay);
            localCache.put(homestay.getId(), homestay);
        }
        return homestay;
    }

    @Override
    public void deleteById(Long homestayId) {
        homestayDomainRepository.deleteById(homestayId);
        redisCache.delete(RedisKey.HOMESTAY.getKey() + homestayId);
        localCache.invalidate(homestayId);
    }

    @Override
    public Homestay update(Homestay homestay) {
        RedisDistributedLocker locker = distributedCache.getDistributedLocker(LockKey.HOMESTAY.getKey() + homestay.getId());
        try {
            boolean isLocked = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new DomainDistributedLockException("Đối tượng đang được sử dụng, vui lòng thử lại sau");
            }

            return updateWithCache(homestay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    private Homestay getHomestayFromLocalCache(Long homestayId) {
        Homestay homestay = localCache.getIfPresent(homestayId);
        if (homestay != null) {
            log.info("GET HOMESTAY {} FROM LOCAL CACHE", homestayId);
        }
        return homestay;
    }

    private Homestay getHomestayFromGlobalCache(Long homestayId, String cacheKey) {
        Homestay homestay = redisCache.getObject(cacheKey, Homestay.class);
        if (homestay != null) {
            log.info("GET HOMESTAY {} FROM DISTRIBUTED CACHE", homestayId);
            localCache.put(homestayId, homestay);
        }
        return homestay;
    }

    private Homestay getHomestayFromDatabaseWithLock(Long homestayId, String cacheKey) {
        RedisDistributedLocker locker = distributedCache.getDistributedLocker(LockKey.HOMESTAY.getKey() + homestayId);
        try {
            boolean isLocked = locker.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLocked) {
                return null;
            }

            Homestay homestay = getHomestayFromGlobalCache(homestayId, cacheKey);
            if (homestay != null) {
                return homestay;
            }

            homestay = homestayDomainRepository.findById(homestayId);
            if (homestay == null) {
                throw new DomainNotFoundException("Không tìm thấy homestay với id: " + homestayId);
            }

            log.info("GET HOMESTAY {} FROM DATABASE", homestayId);
            redisCache.setObject(cacheKey, homestay);
            localCache.put(homestayId, homestay);
            return homestay;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    private Homestay updateWithCache(Homestay homestay) {
        homestay = homestayDomainRepository.update(homestay);
        if (homestay != null) {
            redisCache.setObject(RedisKey.HOMESTAY.getKey() + homestay.getId(), homestay);
            localCache.put(homestay.getId(), homestay);
        }
        return homestay;
    }
}
