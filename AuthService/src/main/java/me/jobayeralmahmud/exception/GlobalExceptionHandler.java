package me.jobayeralmahmud.exception;

import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import me.jobayeralmahmud.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Validation failed",  errors));
    }

    // 2. Handles URL Parameter Validation (@PathVariable, @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            // Extracts the field name from the path (e.g., "getProduct.id" -> "id")
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

    // Catch-all for unexpected server errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<ApiResponse<Void>> buildResponse(HttpStatus httpStatus, String exceptionMessage) {
        return ResponseEntity
                .status(httpStatus)
                .body(ApiResponse.error(httpStatus.value(), exceptionMessage));
    }
}