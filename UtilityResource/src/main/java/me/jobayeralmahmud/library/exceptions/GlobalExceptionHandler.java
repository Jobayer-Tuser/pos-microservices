package me.jobayeralmahmud.library.exceptions;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import me.jobayeralmahmud.library.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Map<String, String> errors = new HashMap<>();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException exception) {

        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Validation failed",  errors));
    }

    // 2. Handles URL Parameter Validation (@PathVariable, @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(ConstraintViolationException ex) {
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = "";
            for (Path.Node node : violation.getPropertyPath()) {
                fieldName = node.getName();
            }
            errors.put(fieldName, violation.getMessage());
        });

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "Parameter validation failed", errors));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserExists(UserAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "User name or email already exists.");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Please check your email and password match or not or check the user role and permission assigned or not.");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(NullPointerException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Jwt token invalid or expired please regenerate a new one" + ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> authorizationDeniedException(AuthorizationDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "You don't have permission to access this resource.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> accessDeniedException(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    protected ResponseEntity<ApiResponse<Void>> buildResponse(HttpStatus httpStatus, String exceptionMessage) {
        return ResponseEntity.status(httpStatus)
                .body(ApiResponse.error(httpStatus.value(), exceptionMessage));
    }
}