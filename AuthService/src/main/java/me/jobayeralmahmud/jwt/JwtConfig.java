package me.jobayeralmahmud.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.jwt")
public record JwtConfig(
        String secretKey,
        int accessTokenExpiration,
        int refreshTokenExpiration
) {}
