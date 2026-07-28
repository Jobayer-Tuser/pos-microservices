package me.jobayeralmahmud.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.auth.config.Routes;
import me.jobayeralmahmud.auth.jwt.JwtResponse;
import me.jobayeralmahmud.auth.request.CreateUserRequest;
import me.jobayeralmahmud.auth.request.LoginRequest;
import me.jobayeralmahmud.auth.response.UserDto;
import me.jobayeralmahmud.auth.service.AuthService;
import me.jobayeralmahmud.auth.service.SecuredUser;
import me.jobayeralmahmud.auth.service.UserService;
import me.jobayeralmahmud.auth.service.UserVerificationService;
import me.jobayeralmahmud.library.annotations.ApiResponseMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping(Routes.Auth.BASE)
public class AuthController extends Controller {

    private final AuthService authService;
    private final UserService userService;
    private final UserVerificationService userVerificationService;

    @PostMapping(Routes.Auth.LOGIN)
    @ApiResponseMessage("success.user.login")
    public JwtResponse login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        var accessToken = authService.authenticateUser(request, response);
        return new JwtResponse(accessToken);
    }

    @PostMapping(Routes.Auth.REGISTER)
    @ApiResponseMessage("success.user.register")
    public UserDto register(@Valid @RequestBody CreateUserRequest request)
    {
        return userService.createUser(request);
    }

    @PostMapping(Routes.Auth.TOKEN_REFRESH)
    public JwtResponse refresh(@CookieValue(value = "refreshToken") String refreshToken)
    {
        var accessToken = authService.refreshToken(refreshToken);
        return new JwtResponse(accessToken);
    }

    @GetMapping(Routes.Auth.EMAIL_VERIFY)
    @ApiResponseMessage("success.user.verify-email")
    public void verifyEmail(@RequestParam("token") String token)
    {
        userVerificationService.verifyUserEmail(token);
    }

    @GetMapping(Routes.User.VALIDATED_PROFILE)
    @PreAuthorize("hasRole('USER')")
    public HashMap<String, Object> getAuthenticatedUserProfile(@AuthenticationPrincipal SecuredUser user) {
        var data = new HashMap<String, Object>();
        data.put("role", user.getAuthorities());
        return data;
    }
}