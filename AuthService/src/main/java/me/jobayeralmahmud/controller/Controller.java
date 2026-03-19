package me.jobayeralmahmud.controller;

import me.jobayeralmahmud.service.SecuredUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;
import me.jobayeralmahmud.library.controller.BaseController;

import java.util.UUID;

public class Controller extends BaseController {

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
        return (auth.getPrincipal() instanceof SecuredUser u) ? u.getUserId() : null;
    }
}
