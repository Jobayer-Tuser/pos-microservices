package me.jobayeralmahmud.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.service.RedisService;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtParser {
    private final Claims claims;
    private final SecretKey secretKey;

    public UUID getSubject() {
        return UUID.fromString(claims.getSubject());
    }

    public String getEmail() {
        return claims.get("email", String.class);
    }

    public long remainExpireTime() {
        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    public boolean isTokenExpired() {
        try {
            return claims.getExpiration().before(new Date());
        } catch (JwtException e) {
            return true;
        }
    }

    public boolean isValidToken(UserDetails user, RedisService redisService) {
        try {
            boolean isBlackListed = redisService.isBlackListed(getSubject().toString());
            return Optional.ofNullable(getEmail()).isPresent()
                    && getEmail().equals(user.getUsername())
                    && !isTokenExpired()
                    && !isBlackListed;
        } catch (JwtException e) {
            log.error("Validating token failed: {}", e.getMessage());
            return false;
        }
    }
}
