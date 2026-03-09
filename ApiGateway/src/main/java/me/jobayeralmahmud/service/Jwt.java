package me.jobayeralmahmud.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

public class Jwt {

    private final Claims claims;
    private final SecretKey secretKey;

    public Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    public boolean isExpire() {
        return claims.getExpiration().before(new Date());
    }

    public UUID getUserId() {
        return UUID.fromString(claims.getSubject());
    }

    public String getUserPermissions() {
        return claims.get("permissions", String.class);
    }

    public String getRole(){
        return String.valueOf(
                claims.get("role", String.class)
        );
    }

    public String toString() {
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
    }
}
