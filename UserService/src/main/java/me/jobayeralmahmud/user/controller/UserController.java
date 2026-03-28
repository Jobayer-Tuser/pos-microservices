package me.jobayeralmahmud.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.response.CursorPageResponse;
import me.jobayeralmahmud.user.config.Routes;
import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.user.request.CreateUserProfileRequest;
import me.jobayeralmahmud.user.request.GetUserProfileRequest;
import me.jobayeralmahmud.user.request.UpdateUserProfileRequest;
import me.jobayeralmahmud.user.response.UserProfileDto;
import me.jobayeralmahmud.user.service.UserProfileService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> index(GetUserProfileRequest request) {
        return ok_list(userProfileService.collectUsers(request), "Successfully retrieve the user details please check the list!");
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> store(@Valid @RequestBody CreateUserProfileRequest request) {
        return created(userProfileService.createUserProfile(request), "User profile created successfully");
    }

    @PatchMapping(Routes.UPDATE_USER_PROFILE + "/{id}")
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    public ResponseEntity<?> update(@Valid @PathVariable UUID id, @RequestBody UpdateUserProfileRequest request) throws AccessDeniedException {
        return ok(userProfileService.updateUserProfile(id, request, presentUserId()), "User profile updated successfully");
    }

    @GetMapping("/role")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> confirmRole(@RequestHeader("X-User-Role") String role) {
        return ResponseEntity.ok(role);
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('READ_POST')")
    public ResponseEntity<List<String>> confirmPermission( @RequestHeader("X-User-Permissions") List<String> permissions) {
        return ResponseEntity.ok(permissions);
    }
}
