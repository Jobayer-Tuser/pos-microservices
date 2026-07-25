package me.jobayeralmahmud.auth.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.jobayeralmahmud.auth.annotations.validator.EmailExistsValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = EmailExistsValidator.class)
public @interface ExistsEmail {
    String message() default "Email is already registered";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}