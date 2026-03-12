package me.jobayeralmahmud.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.config.Routes;
import me.jobayeralmahmud.dto.request.LoginRequest;
import me.jobayeralmahmud.dto.request.CreateUserRequest;
import me.jobayeralmahmud.dto.response.ApiResponse;
import me.jobayeralmahmud.dto.response.UserDto;
import me.jobayeralmahmud.jwt.JwtResponse;
import me.jobayeralmahmud.service.SecuredUser;
import me.jobayeralmahmud.service.AuthService;
import me.jobayeralmahmud.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Routes.AUTH_SERVICE)
@RequiredArgsConstructor
public class AuthController extends Controller {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping(Routes.LOGIN)
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var accessToken = authService.authenticateUser(request, response);
        return ok(new JwtResponse(accessToken), "Successful login");
    }

    @PostMapping(Routes.REGISTER)
    public ResponseEntity<ApiResponse<UserDto>> register(@Valid @RequestBody CreateUserRequest request) {
        UserDto user = userService.createUser(request);
        return created(user, "Successfully created user please check your email to email verify!");
    }

    @PostMapping(Routes.TOKEN_REFRESH)
    public ResponseEntity<ApiResponse<JwtResponse>> refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        var accessToken = authService.refreshToken(refreshToken);
        return ok(new JwtResponse(accessToken),  "Successfully refreshed token");
    }

    @GetMapping(Routes.VALIDATED_PROFILE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> getAuthenticatedUserProfile(@AuthenticationPrincipal SecuredUser user) {
        var data = new HashMap<String, Object>();
        data.put("role", user.getAuthorities());
        return ok(data, "Successfully received the data");
    }
}
