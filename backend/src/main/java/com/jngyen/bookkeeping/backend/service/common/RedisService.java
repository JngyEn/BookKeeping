package com.jngyen.bookkeeping.backend.service.common;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Resource
    private  StringRedisTemplate stringRedisTemplate;


    public  void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Map<String,Object> m) {
        stringRedisTemplate.opsForHash().putAll(key, m);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public Map<Object, Object> getMap(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    public void expire(String key, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.expire(key, timeout,timeUnit);
    }
    // 存在返回true，否则返回false
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
}
