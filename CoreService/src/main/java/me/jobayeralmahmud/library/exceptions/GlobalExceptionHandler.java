package me.jobayeralmahmud.library.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handles validation for @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {

        var filedErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError ->
                                Objects.requireNonNull(fieldError.getDefaultMessage(), "Invalid value please check again!"),
                        (existing, _) -> existing
                ));

        return createProblemDetail(
                "Validation Failed",
                "One or more request fields failed validation!",
                filedErrors
        );
    }

    // 2. Handles URL Parameter Validation (@PathVariable, @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        var violations = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, _) -> existing
                ));

        return createProblemDetail(
                "Parameter constraint failure!",
                "URL parameters failed validation constraints.",
                violations
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserExists(UserAlreadyExistsException ex)
    {
        return createProblemDetail(
                HttpStatus.CONFLICT,
                "User already exists.",
                ex.getMessage()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex)
    {
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Authentication failed.",
                "Invalid email, password or user role and permission not assigned."
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex)
    {
        return createProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Unauthorize Access",
                "Authentication token is invalid, expired or missing"
        );
    }

    @ExceptionHandler({ AccessDeniedException.class, AuthorizationDeniedException.class })
    public ProblemDetail handleAccessDenied(Exception ex)
    {
        log.error(ex.getMessage(), ex);

        return createProblemDetail(
                HttpStatus.FORBIDDEN,
                "Access Denied",
                "You do not permission to access this resource."
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex)
    {
        log.error(ex.getMessage(), ex);

        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected internal error occurred. Please contact support if the issue persists."
        );
    }

    private ProblemDetail createProblemDetail(String title, String details, Map<String, String> fieldErrors)
    {
        var problem = createProblemDetail(HttpStatus.BAD_REQUEST, title, details);
        problem.setProperty("fieldErrors", fieldErrors);
        return problem;
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String details)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, details);
        problem.setTitle(title);
        return problem;
    }
}