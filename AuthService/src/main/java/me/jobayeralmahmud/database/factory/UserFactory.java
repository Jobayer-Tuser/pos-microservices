package me.jobayeralmahmud.database.factory;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory extends Factory<User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User definition() {
        return User.builder()
            .email(faker.internet().emailAddress())
            .password(passwordEncoder.encode("SuperSecretPass"))
            .build();
    }
}
