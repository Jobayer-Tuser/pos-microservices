package me.jobayeralmahmud.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.library.utils.Messages;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class ApplicationLogoutSuccessHandler implements LogoutSuccessHandler {
    private final ObjectMapper objectMapper;

    public ApplicationLogoutSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onLogoutSuccess(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @Nullable Authentication authentication
    ) throws IOException {
        SecurityContextHolder.clearContext();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        var jsonResponse = objectMapper.writeValueAsString(
                ApiResponse.success(null, Messages.get("success.user.logout")));

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}