package me.jobayeralmahmud.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.jwt")
public record JwtConfig(
        String secretKey
) {}
