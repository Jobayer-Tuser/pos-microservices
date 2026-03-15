package me.jobayeralmahmud.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Jwt {

    private final Claims claims;
    private final SecretKey secretKey;

    public boolean isExpire() {
        return claims.getExpiration().before(new Date());
    }

    public UUID getUserId() {
        return UUID.fromString(claims.getSubject());
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

    public String getRole(){
        return claims.get("role", String.class);
    }

    public String toString() {
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }
}
