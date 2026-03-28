package me.jobayeralmahmud.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank(message = "username is required!")
        String username,

        @NotBlank(message = "email is required!")
        @Email(message = "Invalid email format!")
        String email,
        Long roleId
) {
}
