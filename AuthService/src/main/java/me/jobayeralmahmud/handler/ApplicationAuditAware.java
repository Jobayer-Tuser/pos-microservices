package me.jobayeralmahmud.handler;

import me.jobayeralmahmud.service.SecuredUser;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class ApplicationAuditAware implements AuditorAware<UUID> {

    @Override @NullMarked
    public Optional<UUID> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if ( authentication == null ||
                ! authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken ) {
            return Optional.empty();
        }

        SecuredUser principal = (SecuredUser) authentication.getPrincipal();
        return Optional.ofNullable(principal.getUserId());
    }
}
