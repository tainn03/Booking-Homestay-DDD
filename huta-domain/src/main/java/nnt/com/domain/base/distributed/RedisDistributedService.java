package nnt.com.domain.base.distributed;

public interface RedisDistributedService {
    RedisDistributedLocker getDistributedLocker(String lockKey);
}
