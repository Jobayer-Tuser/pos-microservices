package me.jobayeralmahmud.user.request;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        Long roleId
) {
}
