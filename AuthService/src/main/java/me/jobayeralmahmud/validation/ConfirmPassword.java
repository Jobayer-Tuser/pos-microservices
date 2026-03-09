package me.jobayeralmahmud.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.jobayeralmahmud.validation.validator.ConfirmPasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfirmPasswordValidator.class)
public @interface ConfirmPassword {
    String message() default "Confirm Passwords do not match!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
