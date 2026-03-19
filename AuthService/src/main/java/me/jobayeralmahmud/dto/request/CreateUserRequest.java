package me.jobayeralmahmud.dto.request;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        Long roleId
) { }
