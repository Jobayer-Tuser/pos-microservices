package me.jobayeralmahmud.auth.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record UserIdentityHeader(String userId, String role, String permissions) {

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        putIfNotNull(headers, "X-User-Id", userId);
        putIfNotNull(headers, "X-User-Role", role);
        putIfNotNull(headers, "X-User-Permissions", permissions);
        return headers;
    }

    private void putIfNotNull(Map<String, String> headers, String key, String value) {
        Optional.ofNullable(value)
                .filter(header -> !header.isBlank())
                .ifPresent(v -> headers.put(key, v));
    }
}
