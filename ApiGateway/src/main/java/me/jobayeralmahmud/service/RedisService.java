package me.jobayeralmahmud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:user:";

    public boolean isBlackListed(String jti) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }
}