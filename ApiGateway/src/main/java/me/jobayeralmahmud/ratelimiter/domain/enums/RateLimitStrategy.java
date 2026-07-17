package me.jobayeralmahmud.ratelimiter.domain.enums;

public enum RateLimitStrategy {
    FIXED_WINDOW,
    SLIDING_WINDOW,
    TOKEN_BUCKET,
    LEAKY_BUCKET
}