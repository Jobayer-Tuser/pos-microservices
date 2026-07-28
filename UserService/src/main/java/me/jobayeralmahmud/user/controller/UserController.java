package me.jobayeralmahmud.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.annotations.ApiResponseMessage;
import me.jobayeralmahmud.library.response.CursorPageResponse;
import me.jobayeralmahmud.user.config.Routes;
import me.jobayeralmahmud.user.request.CreateUserProfileRequest;
import me.jobayeralmahmud.user.request.UpdateUserProfileRequest;
import me.jobayeralmahmud.user.response.UserProfileDto;
import me.jobayeralmahmud.user.response.UserProfileSummary;
import me.jobayeralmahmud.user.service.UserProfileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
 @RequestMapping(Routes.USER_SERVICE)
@RequiredArgsConstructor
public class UserController extends Controller {

    private final UserProfileService userProfileService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiResponseMessage("Successfully retrieve the user details please check the list!")
    public CursorPageResponse<UserProfileSummary> index(
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "0") int size,
            @RequestParam(required = false) Long lastId
    ) {
        return userProfileService.collectUsers(sortBy, pageSize, lastId);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @ApiResponseMessage("User profile created successfully")
    public UserProfileDto store(@Valid @RequestBody CreateUserProfileRequest request) {
        return userProfileService.createUserProfile(request);
    }

    @PatchMapping(Routes.UPDATE_USER_PROFILE + "/{id}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @ApiResponseMessage("User profile updated successfully")
    public UserProfileDto update(
            @Valid @PathVariable UUID id,
            @RequestBody UpdateUserProfileRequest request
    ) throws AccessDeniedException {
        return userProfileService.updateUserProfile(id, request, presentUserId());
    }

    @GetMapping("/role")
    @PreAuthorize("hasRole('USER')")
    public String confirmRole(@RequestHeader("X-User-Role") String role) {
        return role;
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('READ_POST')")
    public List<String> confirmPermission( @RequestHeader("X-User-Permissions") List<String> permissions) {
        return permissions;
    }
}