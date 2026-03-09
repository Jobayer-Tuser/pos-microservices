package me.jobayeralmahmud.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.config.Routes;
import me.jobayeralmahmud.dto.request.LoginRequest;
import me.jobayeralmahmud.entity.User;
import me.jobayeralmahmud.jwt.Jwt;
import me.jobayeralmahmud.jwt.JwtConfig;
import me.jobayeralmahmud.jwt.JwtService;
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
public class AuthService {

    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UUID getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof SecuredUser securedUser) {
            return securedUser.getUserId();
        }

        throw new IllegalStateException("Cannot find user ID in the current security context");
    }

    public User getCurrentUser() {

        return userService.getUserById(getCurrentUserId());
    }

    public String authenticateUser(LoginRequest request, HttpServletResponse response) {
        Authentication authRequest = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(authRequest);

        var user         = userService.getUserByEmail(request.email());
        var accessToken  = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        setCookie(refreshToken.toString(), response);

        return accessToken.toString();
    }

    public String refreshToken(String refreshToken) {
        Jwt jwt = jwtService.parseToken(refreshToken);

        if (jwt == null || jwt.isExpire()) {
            throw new BadCredentialsException("Jwt token is expired or not valid please provide valid token");
        }

        var user = userService.getUserById(jwt.getUserId());
        var accessToken = jwtService.generateAccessToken(user);
        return accessToken.toString();
    }

    private void setCookie(String refreshToken, HttpServletResponse response) {
        ResponseCookie resCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .path(Routes.AUTH_SERVICE + Routes.LOGIN)
                .maxAge(604800)
                .secure(false)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, resCookie.toString());
    }

    private void setCookieWithJakarta(String refreshToken, HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/login");
        cookie.setMaxAge(jwtConfig.refreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}