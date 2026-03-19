package me.jobayeralmahmud.user.controller;

import me.jobayeralmahmud.library.controller.BaseController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.UUID;

public class Controller extends BaseController {

    protected UUID presentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && authentication.isAuthenticated()) {
            return UUID.fromString(authentication.getName());
        }
        
        throw new IllegalStateException("Cannot find user in the current security context");
    }
}
