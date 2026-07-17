package me.jobayeralmahmud.ratelimiter.domain.model;

import me.jobayeralmahmud.ratelimiter.domain.enums.TimeUnit;

public record RateLimitWindow(Long size, TimeUnit timeUnit) {
}