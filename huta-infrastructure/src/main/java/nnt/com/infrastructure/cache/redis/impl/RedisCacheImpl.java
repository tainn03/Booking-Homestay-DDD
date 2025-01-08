package nnt.com.infrastructure.cache.redis.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import nnt.com.infrastructure.cache.redis.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class RedisCacheImpl implements RedisCache {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCacheImpl() {
        // Khởi tạo ObjectMapper với module JavaTimeModule để chuyển đổi thời gian
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void setString(String key, String value) {
        if (!StringUtils.hasLength(key)) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getString(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(Object::toString)
                .orElse(null);
    }

    @Override
    public void setObject(String key, Object value) {
        if (!StringUtils.hasLength(key)) {
            return;
        }
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue);
        } catch (Exception e) {
            log.error("ERROR WHEN SET OBJECT TO REDIS: {}", e.getMessage());
        }
    }

    @Override
    public <T> T getObject(String key, Class<T> targetClass) {
        Object result = redisTemplate.opsForValue().get(key);
        switch (result) {
            case null -> {
                return null;
            }
            case Map map -> {
                try {
                    // Nếu kết quả là một LinkedHashMap, chuyển đổi nó thành đối tượng mục tiêu
                    return objectMapper.convertValue(result, targetClass);
                } catch (IllegalArgumentException e) {
                    log.error("ERROR WHEN DESERIALIZE MAP TO OBJECT: {}", e.getMessage());
                    return null;
                }
            }
            case String s -> {
                try {
                    // Nếu result là String, thực hiện chuyển đổi bình thường
                    return objectMapper.readValue((String) result, targetClass);
                } catch (JsonProcessingException e) {
                    log.error("ERROR WHEN DESERIALIZE STRING TO OBJECT: {}", e.getMessage());
                    return null;
                }
            }
            default -> {
            }
        }
        return null; // hoặc ném ra một ngoại lệ tùy ý
    }

    @Override
    public void delete(String key) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            log.info("DELETE KEY: {}", key);
            redisTemplate.delete(key);
            if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                log.info("KEY: {} SUCCESSFULLY DELETED", key);
            } else {
                log.warn("FAILED TO DELETE KEY: {}", key);
            }
        } else {
            log.warn("KEY: {} NOT FOUND", key);
        }
    }
}
