package me.jobayeralmahmud.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.entity.Permission;
import me.jobayeralmahmud.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig config;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, config.accessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user) {
        return generateToken(user, config.refreshTokenExpiration());
    }

    public Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(claims, encryptSecreteKey());
        } catch (JwtException exception){
            return null;
        }
    }

    public Claims getClaimsEvenIfExpired(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(encryptSecreteKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private Jwt generateToken(User user, long tokenExpire) {
        Claims claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email",  user.getEmail())
                .add("role",  user.getRole().getName())
                .add("permissions", getPermissions(user))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpire))
                .build();

        return new Jwt(claims, encryptSecreteKey());
    }

    private static @NonNull List<String> getPermissions(User user) {
        return user.getRole().getPermissions().stream().map(Permission::getName).toList();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(encryptSecreteKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public SecretKey encryptSecreteKey()
    {
        return Keys.hmacShaKeyFor(config.secretKey().getBytes());
    }

}
