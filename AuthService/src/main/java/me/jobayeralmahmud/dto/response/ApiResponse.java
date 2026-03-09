package me.jobayeralmahmud.dto.response;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiResponse<T>(
        boolean success,
        int status,
        String message,
        T data,
        Map<String, Object> metadata
) {
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, HttpStatus.OK.value(), message, data, Map.of("timestamp", LocalDateTime.now()));
    }

    // Overload for 201 Created
    public static <T> ApiResponse<T> created(T data, String message) {
        return new ApiResponse<>(true, HttpStatus.CREATED.value(), message, data, Map.of("timestamp", LocalDateTime.now()));
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(false, status, message, null, Map.of("timestamp", LocalDateTime.now()));

    }

    public static <T> ApiResponse<T> error(int status, String message, T data) {
        return new ApiResponse<>(false, status, message, data, Map.of("timestamp", LocalDateTime.now()));
    }
}