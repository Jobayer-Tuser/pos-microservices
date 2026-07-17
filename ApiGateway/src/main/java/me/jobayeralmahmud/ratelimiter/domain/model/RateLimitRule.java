package me.jobayeralmahmud.ratelimiter.domain.model;

import me.jobayeralmahmud.ratelimiter.domain.enums.RateLimitStrategy;
import me.jobayeralmahmud.ratelimiter.domain.enums.RateLimitTarget;

public record RateLimitRule(
        String ruleId,
        RateLimitStrategy strategy,
        RateLimitTarget target,
        long limit,
        RateLimitWindow window,
        boolean enabled
) {
}