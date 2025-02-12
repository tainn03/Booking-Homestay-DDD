package nnt.com.application.service.search.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.search.HomestaySearchAppService;
import nnt.com.domain.aggregates.model.document.HomestayDocument;
import nnt.com.domain.aggregates.service.HomestaySearchDomainService;
import nnt.com.domain.aggregates.service.LocationSearchDomainService;
import nnt.com.infrastructure.cache.redis.RedisCache;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomestaySearchAppServiceImpl implements HomestaySearchAppService {
    HomestaySearchDomainService homestaySearchDomainService;
    LocationSearchDomainService locationSearchDomainService;
    RedisCache redisCache;

    @Override
    public List<HomestayDocument> searchByContent(String content) {
        String key = "searchByContent:" + content;
        if (redisCache.hasKey(key)) {
            return redisCache.getObject(key, List.class);
        }
        List<HomestayDocument> result = homestaySearchDomainService.searchByContent(content);
        redisCache.setObject(key, result, 1L, TimeUnit.DAYS);
        return result;
    }

    @Override
    public List<HomestayDocument> searchByLocation(String lat, String lon, int distance) {
        String key = "searchByLocation:" + lat + ":" + lon + ":" + distance;
        if (redisCache.hasKey(key)) {
            return redisCache.getObject(key, List.class);
        }
        List<HomestayDocument> result = homestaySearchDomainService.searchByLocation(Double.parseDouble(lat), Double.parseDouble(lon), distance);
        redisCache.setObject(key, result, 1L, TimeUnit.DAYS);
        return result;
    }

    @Override
    public List<String> searchByAddress(String address) {
        String key = "searchByAddress:" + address;
        if (redisCache.hasKey(key)) {
            return redisCache.getObject(key, List.class);
        }
        List<String> result = locationSearchDomainService.searchByAddress(address).stream()
                .map(location -> location.getWard() + ", " + location.getDistrict() + ", " + location.getCity())
                .limit(10)
                .toList();
        redisCache.setObject(key, result, 1L, TimeUnit.DAYS);
        return result;
    }
}
