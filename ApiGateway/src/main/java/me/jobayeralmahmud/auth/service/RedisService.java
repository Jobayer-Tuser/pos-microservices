package me.jobayeralmahmud.auth.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.auth.dto.UserContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:user:";
    private static final String USER_CONTEXT_PREFIX = "ctx:user:";

    public boolean isBlackListed(String jti) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(BLACKLIST_PREFIX + jti));
    }

    public UserContext getUserContext(String jti) {
        return (UserContext) redisTemplate.opsForValue().get(USER_CONTEXT_PREFIX + jti);
    }
}