package me.jobayeralmahmud.exceptions;

import me.jobayeralmahmud.auth.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BearerTokenException.class)
    public ResponseEntity<ApiResponse<Void>> bearerTokenException(BearerTokenException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
//        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<Void>> handleNullPointerException(NullPointerException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Jwt token invalid or expired please regenerate a new one" + ex.getMessage());
    }

    private ResponseEntity<ApiResponse<Void>> buildResponse(HttpStatus httpStatus, String exceptionMessage) {
        return ResponseEntity.status(httpStatus)
                .body(ApiResponse.error(httpStatus.value(), exceptionMessage));
    }
}