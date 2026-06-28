package me.jobayeralmahmud.user.controller;

import jakarta.servlet.ServletException;
import jakarta.validation.Validator;
import me.jobayeralmahmud.library.controller.BaseHandler;
import me.jobayeralmahmud.user.request.CreateUserProfileRequest;
import me.jobayeralmahmud.user.request.UpdateUserProfileRequest;
import me.jobayeralmahmud.user.service.UserProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class UserHandler extends BaseHandler {

    private final UserProfileService userProfileService;

    public UserHandler(Validator validator, UserProfileService userProfileService) {
        super(validator);
        this.userProfileService = userProfileService;
    }

    private UUID presentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication) && authentication.isAuthenticated()) {
            return UUID.fromString(authentication.getName());
        }
        throw new IllegalStateException("Cannot find user in the current security context");
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ServerResponse index(ServerRequest request) {
        String sortBy = request.param("sortBy").orElse("id");
        int pageSize = request.param("pageSize").map(Integer::parseInt).orElse(10);
        Long lastId = request.param("lastId").map(Long::parseLong).orElse(null);
        return ok(userProfileService.collectUsers(sortBy, pageSize, lastId), "Successfully retrieve the user details please check the list!");
    }

    @PreAuthorize("hasRole('USER')")
    public ServerResponse store(ServerRequest request) throws ServletException, IOException {
        CreateUserProfileRequest createRequest = request.body(CreateUserProfileRequest.class);
        validate(createRequest);
        return created(userProfileService.createUserProfile(createRequest), "User profile created successfully");
    }

    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ServerResponse update(ServerRequest request) throws ServletException, IOException, AccessDeniedException {
        UUID id = getUUIDPathVariable(request, "id");
        UpdateUserProfileRequest updateRequest = request.body(UpdateUserProfileRequest.class);
        validate(updateRequest);
        return ok(userProfileService.updateUserProfile(id, updateRequest, presentUserId()), "User profile updated successfully");
    }

    @PreAuthorize("hasRole('USER')")
    public ServerResponse confirmRole(ServerRequest request) {
        String role = request.headers().firstHeader("X-User-Role").orElse("");
        return ServerResponse.ok().body(role);
    }

    @PreAuthorize("hasAuthority('READ_POST')")
    public ServerResponse confirmPermission(ServerRequest request) {
        List<String> permissions = request.headers().header("X-User-Permissions");
        if (permissions.size() == 1 && permissions.get(0).contains(",")) {
            permissions = Arrays.asList(permissions.get(0).split("\\s*,\\s*"));
        }
        return ServerResponse.ok().body(permissions);
    }
}
