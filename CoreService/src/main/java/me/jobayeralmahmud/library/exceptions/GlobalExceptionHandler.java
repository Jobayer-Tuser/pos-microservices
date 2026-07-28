package me.jobayeralmahmud.library.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.library.utils.Messages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import java.time.Instant;
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
                                    fieldError -> Objects.requireNonNull(fieldError.getDefaultMessage(),
                                                    "Invalid value please check again!"),
                                    (existing, _) -> existing));

            return createProblemDetail(
                    get("error.request.body.details"),
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
                                    (existing, _) -> existing));

            return createProblemDetail(
                    get("error.request.param.details"),
                    violations
            );
        }

        @ExceptionHandler({ UserAlreadyExistsException.class, CategoryAlreadyExistsException.class })
        public ProblemDetail handleUserExists(Exception ex) {
            return createProblemDetail(
                    HttpStatus.CONFLICT,
                    ex.getMessage()
            );
        }

        @ExceptionHandler({
                        AuthenticationException.class,
                        JwtException.class
        })
        public ProblemDetail handleAuthenticationAndJwtExceptions(Exception ex) {
            String details = switch (ex) {
                    case BadCredentialsException _ -> get("error.auth.failed.details");
                    case ExpiredJwtException _ -> get("error.jwt.token.expired");
                    case JwtException _ -> get("error.jwt.token.invalid");
                    default -> get("error.unauthorize.details");
            };

            return createProblemDetail(HttpStatus.UNAUTHORIZED, details);
        }

        @ExceptionHandler({ AccessDeniedException.class, AuthorizationDeniedException.class })
        public ProblemDetail handleAccessDenied(Exception ex) {
            log.error(ex.getMessage(), ex);

            return createProblemDetail(
                    HttpStatus.FORBIDDEN,
                    get("error.access.denied.details")
            );
        }

        @ExceptionHandler(Exception.class)
        public ProblemDetail handleGeneralException(Exception ex) {
            log.error(ex.getMessage(), ex);

            return createProblemDetail(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    get("error.internal.server.details")
            );
        }

        protected ProblemDetail createProblemDetail(String details, Map<String, String> fieldErrors) {
                var problem = createProblemDetail(HttpStatus.BAD_REQUEST, details);
                problem.setProperty("fieldErrors", fieldErrors);
                return problem;
        }

        protected ProblemDetail createProblemDetail(HttpStatus status, String details) {
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, details);
            problem.setProperty("timestamp", Instant.now().toString());
            return problem;
        }

        private String get(String key) {
            return Messages.get(key);
        }
}