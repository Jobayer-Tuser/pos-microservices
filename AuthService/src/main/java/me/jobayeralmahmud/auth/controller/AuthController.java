package me.jobayeralmahmud.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.auth.config.Routes;
import me.jobayeralmahmud.auth.dto.request.LoginRequest;
import me.jobayeralmahmud.auth.dto.request.CreateUserRequest;
import me.jobayeralmahmud.auth.dto.response.UserDto;
import me.jobayeralmahmud.auth.jwt.JwtResponse;
import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.auth.service.AuthService;
import me.jobayeralmahmud.auth.service.SecuredUser;
import me.jobayeralmahmud.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(Routes.Auth.BASE)
@RequiredArgsConstructor
public class AuthController extends Controller {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping(Routes.Auth.LOGIN)
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var accessToken = authService.authenticateUser(request, response);
        return ok(new JwtResponse(accessToken), "Successful login");
    }

    @PostMapping(Routes.Auth.REGISTER)
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody CreateUserRequest request) {
        UserDto user = userService.createUser(request);
        return created(user, "Successfully created user please check your email to email verify!");
    }

    @PostMapping(Routes.Auth.TOKEN_REFRESH)
    public ResponseEntity<ApiResponse<JwtResponse>> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        var accessToken = authService.refreshToken(refreshToken);
        return ok(new JwtResponse(accessToken),  "Successfully refreshed token");
    }

    @GetMapping(Routes.User.VALIDATED_PROFILE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> getAuthenticatedUserProfile(@AuthenticationPrincipal SecuredUser user) {
        var data = new HashMap<String, Object>();
        data.put("role", user.getAuthorities());
        return ok(data, "Successfully received the data");
    }
}