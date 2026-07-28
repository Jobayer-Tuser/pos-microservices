package me.jobayeralmahmud.gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.gateway.dto.JwtConfig;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtParser {

    private final JwtConfig config;

    public Jwt parseToken(String token) {
        Claims claims = getClaims(token);
        return new Jwt(claims);
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