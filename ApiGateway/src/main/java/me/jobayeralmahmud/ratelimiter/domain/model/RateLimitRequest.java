package me.jobayeralmahmud.ratelimiter.domain.model;

import java.time.Instant;

public record RateLimitRequest(
        ClientIdentifier client,
        String path,
        String method,
        Instant timestamp
) {
}