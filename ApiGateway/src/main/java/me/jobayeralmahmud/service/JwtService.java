package me.jobayeralmahmud.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig config;

    public Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, encryptSecreteKey());
        } catch (JwtException exception) {
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(encryptSecreteKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public SecretKey encryptSecreteKey() {
        return Keys.hmacShaKeyFor(config.secretKey().getBytes());
    }

}
