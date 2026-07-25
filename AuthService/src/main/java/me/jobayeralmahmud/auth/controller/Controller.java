package me.jobayeralmahmud.auth.controller;

import me.jobayeralmahmud.auth.service.SecuredUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.UUID;

public class Controller {

    protected UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof SecuredUser securedUser) {
            return securedUser.getUserId();
        }

        throw new IllegalStateException("Cannot find user in the current security context");
    }

    @ModelAttribute("currentUserId")
    public UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        return (auth.getPrincipal() instanceof SecuredUser u) ? u.getUserId() : null;
    }
}