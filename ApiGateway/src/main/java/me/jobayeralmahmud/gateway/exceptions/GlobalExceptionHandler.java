package me.jobayeralmahmud.gateway.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            JwtException.class,
            ExpiredJwtException.class,
            BearerTokenException.class,
    })
    public ProblemDetail handleAuthenticationException(Exception ex) {
        log.warn("Authentication failed due to : {} ", ex.getMessage());

        record TokenError(String detail, String errorCode) {
        }

        TokenError error = switch (ex) {
            case ExpiredJwtException e -> new TokenError(
                    "Your token has expired. Please log in again or refresh your token.",
                    "TOKEN_EXPIRED");
            case BearerTokenException e -> new TokenError(
                    e.getMessage(),
                    "INVALID_HEADER");
            default -> new TokenError(
                    "The provided authentication token is invalid.",
                    "INVALID_TOKEN");
        };

        return createProblemDetail(HttpStatus.UNAUTHORIZED, error.detail(), error.errorCode());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        log.error(ex.getMessage(), ex);

        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong while processing the request Please try again.",
                "INTERNAL_SERVER_ERROR");
    }

    protected ProblemDetail createProblemDetail(HttpStatus status, String details, String errorCode) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, details);
        problem.setProperty("errorCode", errorCode);
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }
}