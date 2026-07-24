package me.jobayeralmahmud.auth.dto.request;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        Long roleId
) { }
