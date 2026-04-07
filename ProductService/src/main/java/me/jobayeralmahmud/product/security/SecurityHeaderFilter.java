package me.jobayeralmahmud.product.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SecurityHeaderFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
            
        String userId = request.getHeader("X-User-Id");
        String rolesHeader = request.getHeader("X-User-Role");
        String permissionsHeader = request.getHeader("X-User-Permissions");

        if (rolesHeader != null || permissionsHeader != null) {

            List<SimpleGrantedAuthority> authorities = getSimpleGrantedAuthorities(rolesHeader, permissionsHeader);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }
    }

    private static @NonNull List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(String rolesHeader, String permissionsHeader) {
        Stream<SimpleGrantedAuthority> roleStream = (rolesHeader != null && !rolesHeader.isBlank())
                ? Stream.of(new SimpleGrantedAuthority("ROLE_" + rolesHeader.trim()))
                : Stream.empty();

        Stream<SimpleGrantedAuthority> permissionStream = (permissionsHeader != null && !permissionsHeader.isBlank())
                ? Arrays.stream(permissionsHeader.split(","))
                        .map(String::trim)
                        .map(SimpleGrantedAuthority::new)
                : Stream.empty();

        return Stream.concat(roleStream, permissionStream).toList();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !pathMatcher.match("/dev/api/**", request.getServletPath());
    }
}