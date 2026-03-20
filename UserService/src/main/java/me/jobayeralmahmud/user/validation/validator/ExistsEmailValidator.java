package me.jobayeralmahmud.user.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.jobayeralmahmud.user.request.CreateUserProfileRequest;
import me.jobayeralmahmud.user.validation.ConfirmPassword;
import me.jobayeralmahmud.user.validation.ExistsEmail;

public class ExistsEmailValidator implements ConstraintValidator<ExistsEmail, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return true;
    }
}
