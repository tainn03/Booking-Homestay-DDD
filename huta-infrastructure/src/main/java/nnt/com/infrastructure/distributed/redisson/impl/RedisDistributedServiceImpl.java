package nnt.com.infrastructure.distributed.redisson.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.base.distributed.RedisDistributedLocker;
import nnt.com.domain.base.distributed.RedisDistributedService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisDistributedServiceImpl implements RedisDistributedService {
    @Resource
    private RedissonClient redissonClient;

    @Override
    public RedisDistributedLocker getDistributedLocker(String lockKey) {
        RLock rLock = redissonClient.getLock(lockKey);

        return new RedisDistributedLocker() {

            @Override
            public boolean tryLock(long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
                return rLock.tryLock(waitTime, leaseTime, unit);
            }

            @Override
            public void lock(long leaseTime, TimeUnit unit) {
                rLock.lock(leaseTime, unit);
            }

            @Override
            public void unlock() {
                if (isLocked() && isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }

            @Override
            public boolean isLocked() {
                return rLock.isLocked();
            }

            @Override
            public boolean isHeldByThread(long threadId) {
                return rLock.isHeldByThread(threadId);
            }

            @Override
            public boolean isHeldByCurrentThread() {
                return rLock.isHeldByCurrentThread();
            }
        };
    }
}
