package me.jobayeralmahmud.database.seeders;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.database.factory.UserFactory;
import me.jobayeralmahmud.entity.User;
import me.jobayeralmahmud.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserSeeder implements Seeder {

    private final UserRepository userRepository;
    private final UserFactory userFactory;

    @Override
    @Transactional
    public void run() {
        if (userRepository.count() == 0) {
            List<User> users = userFactory.create(100);
            userRepository.saveAll(users);
        }
    }
}
