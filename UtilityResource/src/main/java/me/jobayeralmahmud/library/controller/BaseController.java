package me.jobayeralmahmud.library.controller;

import me.jobayeralmahmud.library.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public abstract class BaseController {

    /**
     * Helper for 200 OK responses
     */
    protected <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }

    /**
     * Helper for 201 Created responses (Perfect for Registration)
     */
    protected <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(data,  message));
    }

    /**
     * Helper for 204 No Content responses (Perfect for Delete)
     */
    protected ResponseEntity<ApiResponse<Void>> noContent(String message) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(null, message));
    }

    protected <T> ResponseEntity<ApiResponse<List<T>>> ok(Page<T> page, String message) {
        Map<String, Object> metadata = Map.of(
                "timestamp", LocalDateTime.now(),
                "currentPage", page.getNumber(),
                "totalPages", page.getTotalPages(),
                "totalElements", page.getTotalElements(),
                "pageSize", page.getSize()
        );

        ApiResponse<List<T>> response = new ApiResponse<>(
                true, HttpStatus.OK.value(), message, page.getContent(), metadata
        );

        return ResponseEntity.ok(response);
    }
}
