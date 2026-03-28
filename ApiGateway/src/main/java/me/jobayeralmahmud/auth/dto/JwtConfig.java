package me.jobayeralmahmud.auth.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.jwt")
public record JwtConfig(
        String secretKey
) {}
