package me.jobayeralmahmud.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtParser {

    private final Claims claims;

    public String getUserId() {
        return UUID.fromString(claims.getSubject()).toString();
    }

    public String getJti() {
        return claims.getId();
    }

    public String getUserPermissions() {
        Object permissionsClaim = claims.get("permissions");

        String permissionsHeader = "";
        if (permissionsClaim instanceof List<?> list) {
            permissionsHeader = list.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
        }

        return permissionsHeader;
    }

    public String getRole() {
        return claims.get("role", String.class);
    }
}
