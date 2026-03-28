package me.jobayeralmahmud.service;

import jakarta.servlet.http.HttpServletResponse;
import me.jobayeralmahmud.dto.request.LoginRequest;
import me.jobayeralmahmud.entity.User;

import java.util.UUID;

public interface AuthService {
    UUID getCurrentUserId();

    User getCurrentUser();

    String authenticateUser(LoginRequest request, HttpServletResponse response);

    String refreshToken(String refreshToken);
}
