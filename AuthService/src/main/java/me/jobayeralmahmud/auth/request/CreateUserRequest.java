package me.jobayeralmahmud.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import me.jobayeralmahmud.auth.annotations.ExistsEmail;
import me.jobayeralmahmud.auth.entity.Role;
import me.jobayeralmahmud.auth.entity.User;

public record CreateUserRequest(
        String username,

        @Email
        @NotBlank
        @ExistsEmail
        String email,

        @NotBlank
        String password,
        Long roleId
) {
    public User toEntity(String encodedPassword, Role role) {
        return User.builder()
                .username(username)
                .email(email)
                .password(encodedPassword)
                .role(role)
                .build();
    }
}