package me.jobayeralmahmud.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SecurityHeaderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String rolesHeader = request.getHeader("X-User-Role");
        System.out.println(rolesHeader);
        String permissionsHeader = request.getHeader("X-User-Permissions");
        System.out.println(permissionsHeader);
        String userId = request.getHeader("X-User-Id"); // Optional but recommended

        if (rolesHeader != null || permissionsHeader != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            // 1. Process Roles (add ROLE_ prefix if not present)
            if (rolesHeader != null && !rolesHeader.isBlank()) {
                authorities.addAll(Arrays.stream(rolesHeader.split(","))
                        .map(role -> new SimpleGrantedAuthority(role.trim()))
                        .toList());
            }

            // 2. Process Permissions
            if (permissionsHeader != null && !permissionsHeader.isBlank()) {
                authorities.addAll(Arrays.stream(permissionsHeader.split(","))
                        .map(perm -> new SimpleGrantedAuthority(perm.trim()))
                        .toList());
            }

            // 3. Set the Security Context
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Important: Clear context after request to prevent thread local leaks
        SecurityContextHolder.clearContext();
    }
}
