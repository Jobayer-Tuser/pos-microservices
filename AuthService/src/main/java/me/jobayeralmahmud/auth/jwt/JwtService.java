package me.jobayeralmahmud.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.auth.entity.Permission;
import me.jobayeralmahmud.auth.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig config;

    public String generateAccessToken(User user) {
        return generateToken(user, config.accessTokenExpiration());
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, config.refreshTokenExpiration());
    }

    public JwtParser parseToken(String token) {
        try {
            Claims claims = getClaims(token);
            return new JwtParser(claims, encryptSecretKey());
        } catch (JwtException e) {
            log.error("Toke might be expired or invalid: {}", e.getMessage());
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(encryptSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateToken(User user, long tokenExpire) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .claims(claimBuilder(user))
                .signWith(encryptSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpire))
                .compact();
    }

    private Claims claimBuilder(User user) {
        return Jwts.claims()
                .add("email", user.getEmail())
                .add("role", user.getRole().getName())
                .add("permissions", getPermissions(user))
                .build();
    }

    private @NonNull List<String> getPermissions(User user) {
        return user.getRole().getPermissions().stream().map(Permission::getName).toList();
    }

    private SecretKey encryptSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(config.secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
