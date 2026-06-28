package me.jobayeralmahmud.store.controller;

import jakarta.servlet.ServletException;
import jakarta.validation.Validator;
import me.jobayeralmahmud.library.controller.BaseHandler;
import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.service.StoreService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class StoreHandler extends BaseHandler {

    private final StoreService storeService;

    public StoreHandler(Validator validator, StoreService storeService) {
        super(validator);
        this.storeService = storeService;
    }

    private UUID currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated()) {
            return UUID.fromString(authentication.getName());
        }
        throw new IllegalStateException("Cannot find user in the current security context");
    }

    public ServerResponse index(ServerRequest request) {
        Pageable pageable = getPageable(request);
        return ok(storeService.findAllStores(pageable), "Stores retrieved successfully");
    }

    public ServerResponse store(ServerRequest request) throws ServletException, IOException {
        StoreCreateRequest createRequest = request.body(StoreCreateRequest.class);
        validate(createRequest);
        return created(storeService.createStore(createRequest, currentUser()), "Store created successfully");
    }

    public ServerResponse update(ServerRequest request) throws ServletException, IOException {
        UUID storeId = getUUIDPathVariable(request, "id");
        StoreUpdateRequest updateRequest = request.body(StoreUpdateRequest.class);
        validate(updateRequest);
        return ok(storeService.updateStore(storeId, updateRequest, currentUser()), "Store updated successfully");
    }

    public ServerResponse show(ServerRequest request) {
        UUID storeId = getUUIDPathVariable(request, "id");
        return ok(storeService.findStoreById(storeId), "Store details retrieved successfully");
    }
}
