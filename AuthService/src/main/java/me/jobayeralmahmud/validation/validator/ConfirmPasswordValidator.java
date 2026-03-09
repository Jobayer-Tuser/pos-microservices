package me.jobayeralmahmud.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.jobayeralmahmud.dto.request.CreateUserRequest;
import me.jobayeralmahmud.validation.ConfirmPassword;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, CreateUserRequest> {

    @Override
    public boolean isValid(CreateUserRequest request, ConstraintValidatorContext context) {
        if (request.password() == null || request.confirmPassword() == null) {
            return false;
        }
        return request.password().equals(request.confirmPassword());
    }
}
