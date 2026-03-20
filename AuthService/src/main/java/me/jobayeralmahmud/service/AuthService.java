package me.jobayeralmahmud.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.config.Routes;
import me.jobayeralmahmud.dto.request.LoginRequest;
import me.jobayeralmahmud.entity.JwtToken;
import me.jobayeralmahmud.entity.User;
import me.jobayeralmahmud.jwt.JwtConfig;
import me.jobayeralmahmud.jwt.JwtService;
import me.jobayeralmahmud.repository.JwtTokenRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenRepository tokenRepository;

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

        revokeAllTokensByUser(user);
        storeUserToken(refreshToken, user);
        setCookie(refreshToken, response);
        return accessToken;
    }

    public String refreshToken(String refreshToken) {

        if (jwtService.isTokenExpired(refreshToken)) {
             throw new BadCredentialsException("Jwt token is expired or not valid please provide valid token");
        }

        var user = userService.getUserById(jwtService.extractUserId(refreshToken));
        return jwtService.generateAccessToken(user);
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

    private void storeUserToken(String token, User user) {
        var buildToken = JwtToken.builder()
                .token(token)
                .isLoggedOut(false)
                .user(user)
                .build();
        tokenRepository.save(buildToken);
    }

    private void revokeAllTokensByUser(User user) {
        List<JwtToken> validTokens = tokenRepository.findAllJwtTokenByUser(user.getId());

        if (!validTokens.isEmpty()) {
            validTokens.forEach(token -> {
                token.setLoggedOut(true);
            });
        }

        tokenRepository.saveAll(validTokens);
    }
}