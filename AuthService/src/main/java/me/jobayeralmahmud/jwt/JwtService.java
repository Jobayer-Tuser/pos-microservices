package me.jobayeralmahmud.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.entity.Permission;
import me.jobayeralmahmud.entity.User;
import me.jobayeralmahmud.repository.JwtTokenRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfig config;
    private final JwtTokenRepository repository;

    public String generateAccessToken(User user) {
        return generateToken(user, config.accessTokenExpiration());
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, config.refreshTokenExpiration());
    }

    public UUID extractUserId(String token) {
        return extractClaim(token, claims -> UUID.fromString(claims.getSubject()));
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    public String extractUserRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    public String extractUserPermissions(String token) {
        return extractClaim(token, claims -> claims.get("permissions", String.class));
    }

    public boolean isValidToken(String token, UserDetails user) {
        var email = extractUserEmail(token);
        var isValidToken = repository.findByToken(token)
                .map( t -> !t.isLoggedOut()).orElse(false);
        return (email.equals(user.getUsername()) && !isTokenExpired(token) && isValidToken);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private SecretKey encryptSecreteKey()  {
        byte [] keyBytes = Decoders.BASE64.decode(config.secretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(encryptSecreteKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateToken(User user, long tokenExpire) {
        Claims claims = claimBuilder(user);
        return Jwts.builder()
                .subject(user.getId().toString())
                .claims(claims)
                .signWith(encryptSecreteKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpire))
                .compact();
    }

    private static Claims claimBuilder(User user) {
        return Jwts.claims()
                .add("email", user.getEmail())
                .add("role", user.getRole().getName())
                .add("permissions", getPermissions(user))
                .build();
    }

    private static @NonNull List<String> getPermissions(User user) {
        return user.getRole().getPermissions().stream().map(Permission::getName).toList();
    }
}
