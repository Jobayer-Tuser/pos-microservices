package me.jobayeralmahmud.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.application")
public record ApiProperties(String apiPrefix) {
}