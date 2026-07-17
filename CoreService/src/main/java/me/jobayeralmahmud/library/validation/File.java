package me.jobayeralmahmud.library.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.jobayeralmahmud.library.enums.FileExtension;
import me.jobayeralmahmud.library.enums.MimeTypes;
import me.jobayeralmahmud.library.validation.validator.FileValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({ FIELD, METHOD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface File {
    String message() default "{constraints.File.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    MimeTypes[] mimeTypes() default {};

    FileExtension[] extensions() default {};

    long maxSize() default Long.MAX_VALUE;
}
