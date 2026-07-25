package me.jobayeralmahmud.auth.annotations.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.auth.annotations.ExistsEmail;
import me.jobayeralmahmud.auth.service.UserService;

@RequiredArgsConstructor
public class EmailExistsValidator implements ConstraintValidator<ExistsEmail, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty())
            return true;

        return !userService.emailExists(value);
    }
}