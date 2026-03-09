package me.jobayeralmahmud.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import me.jobayeralmahmud.enums.UserRole;

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

    public String getUserEmail() {
        return claims.get("email", String.class);
    }

    public String getUserPermissions() {
        return claims.get("permissions", String.class);
    }

    public UserRole getRole(){
        return UserRole.valueOf(
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
