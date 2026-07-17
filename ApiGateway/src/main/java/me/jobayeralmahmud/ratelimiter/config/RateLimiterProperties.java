package me.jobayeralmahmud.ratelimiter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("rate-limiter")
public record RateLimiterProperties(
        long capacity,
        long refillRate,
        int timeout,
        String apiServerUrl
){}