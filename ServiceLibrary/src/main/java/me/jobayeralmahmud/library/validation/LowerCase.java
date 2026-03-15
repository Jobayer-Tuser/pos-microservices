package me.jobayeralmahmud.library.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.jobayeralmahmud.library.validation.validator.LowerCaseValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({FIELD, METHOD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LowerCaseValidator.class)
public @interface LowerCase {
    String message() default "String should be lowercase";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
