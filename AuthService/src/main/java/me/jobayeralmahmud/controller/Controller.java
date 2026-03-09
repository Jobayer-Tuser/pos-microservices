package me.jobayeralmahmud.controller;

import me.jobayeralmahmud.service.SecuredUser;
import me.jobayeralmahmud.dto.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class Controller {

    protected UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof SecuredUser securedUser) {
            return securedUser.getUserId();
        }

        // Throw an exception if this method is called in a non-secured route
        throw new IllegalStateException("Cannot find user in the current security context");
    }

    @ModelAttribute("currentUserId")
    public UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth.getPrincipal() instanceof SecuredUser u) ? u.getUserId() : null;
    }

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
