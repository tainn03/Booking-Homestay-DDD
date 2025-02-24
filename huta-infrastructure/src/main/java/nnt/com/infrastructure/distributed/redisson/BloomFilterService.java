package nnt.com.infrastructure.distributed.redisson;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class BloomFilterService {

    private final RedissonClient redissonClient;
    private RBloomFilter<String> bloomFilter;

    @Value("${bloom.filter.capacity:100000}") // Default: 100,000 elements
    private long capacity;

    @Value("${bloom.filter.errorRate:0.001}") // Default: 0.1% false positive rate
    private double errorRate;

    private static final String FILTER_NAME = "room-availability-bloomfilter";

    private final Map<String, RBloomFilter<String>> bloomFilterMap = new ConcurrentHashMap<>();

    /**
     * Initializes the Bloom Filter when the service starts.
     */
    @PostConstruct
    private void init() {
        try {
            bloomFilter = redissonClient.getBloomFilter(FILTER_NAME);
            if (!bloomFilter.isExists()) {
                bloomFilter.tryInit(capacity, errorRate);
                log.info("INITIALIZED BLOOM FILTER {} WITH CAPACITY {} AND ERROR RATE {}", FILTER_NAME, capacity, errorRate);
            } else {
                log.info("BLOOM FILTER '{}' ALREADY EXISTS", FILTER_NAME);
            }
        } catch (Exception e) {
            log.error("FAILED TO INITIALIZE BLOOM FILTER '{}'", FILTER_NAME, e);
        }
    }

    /**
     * Get or create a Bloom Filter with a unique filter name.
     *
     * @param filterName The unique user ID.
     * @return The Bloom Filter.
     */
    public RBloomFilter<String> getBloomFilter(String filterName) {
        return bloomFilterMap.computeIfAbsent(filterName, id -> {
            String name = "bloomfilter:" + id;
            bloomFilter = redissonClient.getBloomFilter(name);

            if (!bloomFilter.isExists()) {
                bloomFilter.tryInit(capacity, errorRate);
                log.info("INITIALIZED NEW BLOOM FILTER WITH NAME {} AND CAPACITY {} AND ERROR RATE {}", name, capacity, errorRate);
            } else {
                log.info("LOADING EXISTING BLOOM FILTER WITH NAME {}", name);
            }
            return bloomFilter;
        });
    }

    /**
     * Checks if a given key might exist in the Bloom Filter.
     *
     * @param key The key to check.
     * @return True if the key might exist, false if it definitely does not exist.
     */
    public boolean mightContain(String key) {
        if (Objects.isNull(bloomFilter)) {
            log.warn("BLOOM FILTER '{}' IS NOT INITIALIZED!", FILTER_NAME);
            return false;
        }
        return bloomFilter.contains(key);
    }

    /**
     * Adds a key to the Bloom Filter.
     *
     * @param key The key to add.
     */
    public void add(String key) {
        if (Objects.isNull(bloomFilter)) {
            log.warn("BLOOM FILTER '{}' IS NOT INITIALIZED!", FILTER_NAME);
            return;
        }
        bloomFilter.add(key);
        log.info("Added key '{}' to Bloom Filter '{}'", key, FILTER_NAME);
    }
}
