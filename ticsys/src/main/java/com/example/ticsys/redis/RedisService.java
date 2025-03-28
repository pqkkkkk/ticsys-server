package com.example.ticsys.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Lưu dữ liệu vào Redis
    public void saveData(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Lấy dữ liệu từ Redis
    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
