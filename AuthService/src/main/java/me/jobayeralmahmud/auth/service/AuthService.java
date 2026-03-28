package me.jobayeralmahmud.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import me.jobayeralmahmud.auth.dto.request.LoginRequest;
import me.jobayeralmahmud.auth.entity.User;

import java.util.UUID;

public interface AuthService {
    UUID getCurrentUserId();

    User getCurrentUser();

    String authenticateUser(LoginRequest request, HttpServletResponse response);

    String refreshToken(String refreshToken);
}
