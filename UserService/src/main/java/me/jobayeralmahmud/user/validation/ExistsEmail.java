package me.jobayeralmahmud.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.jobayeralmahmud.user.validation.validator.ConfirmPasswordValidator;
import me.jobayeralmahmud.user.validation.validator.ExistsEmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsEmailValidator.class)
public @interface ExistsEmail {
    String message() default "Provided email already exists in our record!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
