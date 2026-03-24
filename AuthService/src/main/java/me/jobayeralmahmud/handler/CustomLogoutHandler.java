package me.jobayeralmahmud.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.jwt.JwtService;
import me.jobayeralmahmud.service.RedisService;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final RedisService redisService;
    private final JwtService jwtService;

    @Override
    public void logout(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @Nullable Authentication authentication) {

        var token = extractTokenFromRequestHeader(request);
        if (Optional.ofNullable(token).isEmpty())
            return;

        setTokenToBlacklist(token);
        clearRefreshTokenCookie(request, response);
    }

    private void setTokenToBlacklist(String token) {
        var jwtParser = jwtService.parseToken(token);
        var jti = jwtParser.getSubject().toString();
        var ttl = jwtParser.remainExpireTime();
        redisService.addToBlacklist(jti, ttl);
    }

    private @Nullable String extractTokenFromRequestHeader(HttpServletRequest request) {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Optional.ofNullable(authHeader).isEmpty() || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

    private void clearRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .ifPresent(cookie -> {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                });
    }
}
