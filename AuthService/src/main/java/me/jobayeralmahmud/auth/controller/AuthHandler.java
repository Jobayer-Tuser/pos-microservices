package me.jobayeralmahmud.auth.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Validator;
import me.jobayeralmahmud.auth.dto.request.LoginRequest;
import me.jobayeralmahmud.auth.dto.request.CreateUserRequest;
import me.jobayeralmahmud.auth.dto.response.UserDto;
import me.jobayeralmahmud.auth.jwt.JwtResponse;
import me.jobayeralmahmud.auth.service.AuthService;
import me.jobayeralmahmud.auth.service.SecuredUser;
import me.jobayeralmahmud.auth.service.UserService;
import me.jobayeralmahmud.library.controller.BaseHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;
import java.util.HashMap;

@Component
public class AuthHandler extends BaseHandler {

    private final AuthService authService;
    private final UserService userService;

    public AuthHandler(Validator validator, AuthService authService, UserService userService) {
        super(validator);
        this.authService = authService;
        this.userService = userService;
    }

    private SecuredUser getSecuredUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecuredUser securedUser) {
            return securedUser;
        }
        throw new IllegalStateException("Cannot find user in the current security context");
    }

    public ServerResponse login(ServerRequest request) throws ServletException, IOException {
        LoginRequest loginRequest = request.body(LoginRequest.class);
        validate(loginRequest);

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes != null ? attributes.getResponse() : null;

        String accessToken = authService.authenticateUser(loginRequest, response);
        return ok(new JwtResponse(accessToken), "Successful login");
    }

    public ServerResponse register(ServerRequest request) throws ServletException, IOException {
        CreateUserRequest createRequest = request.body(CreateUserRequest.class);
        validate(createRequest);
        UserDto user = userService.createUser(createRequest);
        return created(user, "Successfully created user please check your email to email verify!");
    }

    public ServerResponse refresh(ServerRequest request) {
        Cookie cookie = request.cookies().getFirst("refreshToken");
        String refreshToken = cookie != null ? cookie.getValue() : "";
        String accessToken = authService.refreshToken(refreshToken);
        return ok(new JwtResponse(accessToken), "Successfully refreshed token");
    }

    @PreAuthorize("hasRole('USER')")
    public ServerResponse getAuthenticatedUserProfile(ServerRequest request) {
        SecuredUser user = getSecuredUser();
        var data = new HashMap<String, Object>();
        data.put("role", user.getAuthorities());
        return ok(data, "Successfully received the data");
    }
}
