package me.jobayeralmahmud.ratelimiter.strategy;

import me.jobayeralmahmud.ratelimiter.domain.model.RateLimitDecision;
import me.jobayeralmahmud.ratelimiter.domain.model.RateLimitRequest;
import me.jobayeralmahmud.ratelimiter.domain.model.RateLimitRule;
import reactor.core.publisher.Mono;

public interface RateLimiterStrategy {
    Mono<RateLimitDecision> check(RateLimitRequest request, RateLimitRule rule);
}