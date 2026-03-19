package me.jobayeralmahmud.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import me.jobayeralmahmud.library.enums.Gender;
import me.jobayeralmahmud.user.validation.ConfirmPassword;

@ConfirmPassword
public record CreateUserProfileRequest(
        @NotBlank(message = "Username is required!")
        String username,

        @NotBlank(message = "Email is required!")
        @Email(message = "Invalid email format!")
        String email,

        @NotBlank(message = "Password is required!")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must have 8+ chars, uppercase, lowercase, digit, special char!"
        )
        String password,

        @NotBlank(message = "Confirm password is required!")
        String confirmPassword,

        @NotBlank(message = "First name cannot be blank")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        String lastName,
        String displayName,
        String phoneNumber,
        Integer age,
        Gender gender,
        Long roleId
) {
}
