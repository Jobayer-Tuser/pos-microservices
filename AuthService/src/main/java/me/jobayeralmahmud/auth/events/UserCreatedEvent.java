package me.jobayeralmahmud.auth.events;

import lombok.Getter;
import me.jobayeralmahmud.auth.entity.User;
import org.springframework.context.ApplicationEvent;

/**
 * Event published when a new user is created.
 * Listeners can handle verification email sending and other post-creation
 * tasks.
 */
@Getter
public class UserCreatedEvent extends ApplicationEvent {

    private final User user;
    private final String baseUrl;

    public UserCreatedEvent(Object source, User user, String baseUrl) {
        super(source);
        this.user = user;
        this.baseUrl = baseUrl;
    }
}