package me.jobayeralmahmud.library.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        int status,
        String message,
        T data,
        PageMeta page,
        Instant timestamp
) {
    // Convenient factory for standard success payloads
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, 200, message, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return new ApiResponse<>(true, 201, message, data, null, Instant.now());
    }

    public static <T> ApiResponse<T> page(T data, String message, PageMeta page) {
        return new ApiResponse<>(true, 200, message, data, page, Instant.now());
    }
}