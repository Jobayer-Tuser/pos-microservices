package me.jobayeralmahmud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public boolean hasToken(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    public void setToken(String token, String value, long ttl, TimeUnit unit) {
        redisTemplate.opsForValue().set(token, value, ttl, unit);
    }

}
