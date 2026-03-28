package me.jobayeralmahmud.library.controller;

import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.library.response.CursorPageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseController {

    /**
     * Helper for 200 OK responses
     */
    protected <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return buildResponse(HttpStatus.OK, ApiResponse.success(data, message));
    }

    /**
     * Helper for 200 OK responses
     */
    protected <T> ResponseEntity<ApiResponse<?>> ok_list(CursorPageResponse<T> pageResponse, String message) {
        return buildResponse(HttpStatus.OK, ApiResponse.success(
                pageResponse.data(),
                message,
                pageResponse.hasNext(),
                pageResponse.pageSize(),
                pageResponse.nextId()));
    }

    /**
     * Helper for 201 Created responses (Perfect for Registration)
     */
    protected <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return buildResponse(HttpStatus.CREATED, ApiResponse.created(data, message));
    }

    /**
     * Helper for 204 No Content responses (Perfect for Delete)
     */
    protected ResponseEntity<ApiResponse<Void>> noContent(String message) {
        return buildResponse(HttpStatus.NO_CONTENT, ApiResponse.success(null, message));
    }

    private static <T> ResponseEntity<T> buildResponse(HttpStatus status, T body) {
        return ResponseEntity
                .status(status)
                .body(body);
    }
}
