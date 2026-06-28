package me.jobayeralmahmud.auth.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.auth.config.RateLimiterProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTokenBucketService {

    private final RateLimiterProperties rateLimiterProperties;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String TOKEN_KEY_PREFIX = "rate_limiter:tokens:";
    private static final String LAST_REFILL_KEY_PREFIX = "rate_limiter:last_refill:";
}