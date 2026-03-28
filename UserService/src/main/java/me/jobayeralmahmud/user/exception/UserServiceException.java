package me.jobayeralmahmud.user.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.library.exceptions.GlobalExceptionHandler;
import me.jobayeralmahmud.library.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserServiceException extends GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiResponse<Void>> handleFeignClientException(FeignException ex) {
        int status = ex.status();
        String message = ex.contentUTF8();
        log.error("FeignException occurred: status={}, message={}", status, message, ex);

        if (status == 500 && message != null && message.contains("Duplicate entry") && message.contains("pos_users.email")) {
            return buildResponse(HttpStatus.CONFLICT, "Email already exists");
        }

        return switch (status) {
            case 400 -> buildResponse(HttpStatus.BAD_REQUEST, message);
            case 503 -> buildResponse(HttpStatus.SERVICE_UNAVAILABLE, message);
            default ->
                    buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while communicating with the authentication service: " + message );
        };
    }
}
