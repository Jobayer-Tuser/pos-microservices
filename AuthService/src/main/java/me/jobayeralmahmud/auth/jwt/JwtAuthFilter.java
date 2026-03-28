package me.jobayeralmahmud.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.auth.config.Routes;
import me.jobayeralmahmud.auth.service.RedisService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        var requestUri = request.getRequestURI();
        boolean isLogoutPath = requestUri.contains(Routes.Auth.FULL_LOGOUT);
        if (isLogoutPath){
            filterChain.doFilter(request, response);
            return;
        }

        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if ( Optional.ofNullable(authHeader).isEmpty() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token      = authHeader.substring(7);
        var jwtParser  = jwtService.parseToken(token);

        if (jwtParser.isTokenExpired() || redisService.isBlackListed(jwtParser.getJti())) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationIfNot(request, jwtParser.getEmail(), jwtParser, redisService);

        filterChain.doFilter(request, response);
    }

    private void setAuthenticationIfNot(HttpServletRequest request, String email, JwtParser jwtParser, RedisService redisService) {
        if (Optional.ofNullable(email).isPresent() && Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).isEmpty()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtParser.isValidToken(userDetails, redisService)) {
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }
}
