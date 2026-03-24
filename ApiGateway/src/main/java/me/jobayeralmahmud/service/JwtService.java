package me.jobayeralmahmud.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.dto.JwtConfig;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig config;

    public JwtParser parseToken(String token) {
        try {
            Claims claims = getClaims(token);
            return new JwtParser(claims);
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

    private SecretKey encryptSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(config.secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
