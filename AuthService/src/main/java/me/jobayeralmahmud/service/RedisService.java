package me.jobayeralmahmud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:user:";

    public void addToBlacklist(String jti, long ttl) {
        setToken(BLACKLIST_PREFIX + jti, ttl);
    }

    public boolean isBlackListed(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    private void setToken(String key, long ttl) {
        redisTemplate.opsForValue().set(key, "revoked", ttl, TimeUnit.MILLISECONDS);
    }
}