package me.jobayeralmahmud.library.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.library.response.CursorPageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.Set;
import java.util.UUID;

public abstract class BaseHandler {

    protected final Validator validator;

    protected BaseHandler(Validator validator) {
        this.validator = validator;
    }

    protected <T> void validate(T requestBody) {
        if (validator != null && requestBody != null) {
            Set<ConstraintViolation<T>> violations = validator.validate(requestBody);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }
    }

    protected <T> ServerResponse ok(T data, String message) {
        return ServerResponse.ok().body(ApiResponse.success(data, message));
    }

    protected <T> ServerResponse ok(CursorPageResponse<T> pageResponse, String message) {
        return ServerResponse.ok().body(ApiResponse.success(
                pageResponse.data(),
                message,
                pageResponse.hasNext(),
                pageResponse.pageSize(),
                pageResponse.nextId()));
    }

    protected <T> ServerResponse created(T data, String message) {
        return ServerResponse.status(HttpStatus.CREATED).body(ApiResponse.created(data, message));
    }

    protected ServerResponse noContent(String message) {
        return ServerResponse.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(null, message));
    }

    protected UUID getUUIDPathVariable(ServerRequest request, String name) {
        return UUID.fromString(request.pathVariable(name));
    }

    protected Pageable getPageable(ServerRequest request) {
        int page = request.param("page").map(Integer::parseInt).orElse(0);
        int size = request.param("size").map(Integer::parseInt).orElse(10);
        String sortBy = request.param("sortBy").orElse("id");
        return PageRequest.of(page, size, Sort.by(sortBy));
    }
}
