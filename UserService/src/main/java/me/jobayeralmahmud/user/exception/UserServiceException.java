package me.jobayeralmahmud.user.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.library.exceptions.GlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class UserServiceException extends GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ProblemDetail handleFeignClientException(FeignException ex) {
        int status = ex.status();
        String message = ex.contentUTF8();
        log.error("FeignException occurred: status={}, message={}", status, message, ex);

        if (status == 500 && message != null && message.contains("Duplicate entry") && message.contains("pos_users.email")) {
            return createProblemDetail(HttpStatus.CONFLICT, "User already exists please try with different email");
        }

        return switch (status) {
            case 400 -> createProblemDetail(HttpStatus.BAD_REQUEST, message);
            case 503 -> createProblemDetail(HttpStatus.SERVICE_UNAVAILABLE, message);
            default ->
                    createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while communicating with the authentication service" );
        };
    }
}