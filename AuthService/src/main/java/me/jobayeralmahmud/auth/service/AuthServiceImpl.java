package me.jobayeralmahmud.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.auth.dto.request.LoginRequest;
import me.jobayeralmahmud.auth.entity.User;
import me.jobayeralmahmud.auth.jwt.JwtConfig;
import me.jobayeralmahmud.auth.jwt.JwtParser;
import me.jobayeralmahmud.auth.jwt.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final UserService userService;
    private final RedisService redisService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof SecuredUser securedUser) {
            return securedUser.getUserId();
        }

        throw new IllegalStateException("Cannot find user ID in the current security context");
    }

    @Override
    public User getCurrentUser() {
        return userService.getUserById(getCurrentUserId());
    }

    @Override
    public String authenticateUser(LoginRequest request, HttpServletResponse response) {
        Authentication authRequest = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(authRequest);

        var user = userService.getUserByEmail(request.email());
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokensByUser(user);
        setCookie(refreshToken, response);
        return accessToken;
    }

    @Override
    public String refreshToken(String refreshToken) {

        JwtParser jwtParser = jwtService.parseToken(refreshToken);

        if (jwtParser.isTokenExpired() || redisService.isBlackListed(jwtParser.getJti())) {
            throw new BadCredentialsException(
                    "Jwt token is expired or not it is blacklisted please provide valid token.");
        }

        var user = userService.getUserById(jwtParser.getSubject());
        return jwtService.generateAccessToken(user);
    }

    private void setCookie(String refreshToken, HttpServletResponse response) {
        ResponseCookie resCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtConfig.refreshTokenExpiration())
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, resCookie.toString());
    }

    private void revokeAllTokensByUser(User user) {

    }
}