package me.jobayeralmahmud.library.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.jobayeralmahmud.library.validation.validator.ExistsIdValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExistsIdValidator.class)
public @interface ExistsId {
    String message() default "Entity with the given ID does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    Class<?> entity();
}
