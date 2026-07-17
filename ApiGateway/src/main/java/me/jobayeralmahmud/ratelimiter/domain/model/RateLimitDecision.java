package me.jobayeralmahmud.ratelimiter.domain.model;

import me.jobayeralmahmud.ratelimiter.domain.enums.RateLimitStatus;

import java.time.Instant;

public record RateLimitDecision(
        RateLimitStatus status,
        long remainingRequests,
        Instant resetAt,
        String reason
) {
}