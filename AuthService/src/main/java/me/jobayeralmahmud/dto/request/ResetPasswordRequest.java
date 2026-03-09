package me.jobayeralmahmud.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ResetPasswordRequest(
        @NotBlank(message = "password is required!")
        String previousPassword,

        @NotBlank(message = "Confirm password is required!")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must have 8+ chars, uppercase, lowercase, digit, special char!"
        )
        String newPassword
) {}
