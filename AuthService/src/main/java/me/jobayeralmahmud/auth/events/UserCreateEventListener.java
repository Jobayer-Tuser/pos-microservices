package me.jobayeralmahmud.auth.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.auth.service.EmailService;
import me.jobayeralmahmud.auth.service.UserVerificationService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Listener for user-related events.
 * Handles asynchronous tasks like sending verification emails.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreateEventListener {

    private final EmailService emailService;
    private final UserVerificationService verificationService;

    /**
     * Handles user creation event by generating verification token
     * and sending verification email.
     *
     * @param event the user created event
     */
    @Async
    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Handling user created event for user ID: {}", event.getUser().getId());

        try {
            String token = verificationService.generateVerificationToken(event.getUser());
            String verificationUrl = buildVerificationUrl(event.getBaseUrl(), token);

            emailService.sendVerificationEmail(event.getUser(), token, verificationUrl);

            log.info("Verification email sent for user ID: {}", event.getUser().getId());

        } catch (Exception e) {
            log.error("Failed to process user creation event for user ID: {}",
                    event.getUser().getId(), e);
            // Consider implementing retry logic or dead letter queue
        }
    }

    /**
     * Builds the verification URL using the base URL from the event.
     * This avoids accessing ServletRequestAttributes in async context.
     *
     * @param baseUrl the base URL captured from the original request
     * @param token   the verification token
     * @return the complete verification URL
     */
    private String buildVerificationUrl(String baseUrl, String token) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/users/registration/verify")
                .queryParam("token", token)
                .toUriString();
    }
}