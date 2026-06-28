package me.jobayeralmahmud.library.migrations.seeders.factory;

import lombok.RequiredArgsConstructor;
import org.booking.users.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory extends Factory<User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User definition() {
        return User.builder()
            .name(faker.name().name())
            .displayName(faker.name().firstName())
            .email(faker.internet().emailAddress())
            .password(passwordEncoder.encode("SuperSecretPass"))
            .build();
    }
}