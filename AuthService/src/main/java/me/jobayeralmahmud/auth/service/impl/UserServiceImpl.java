package me.jobayeralmahmud.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.auth.entity.Role;
import me.jobayeralmahmud.auth.entity.User;
import me.jobayeralmahmud.auth.events.UserCreatedEvent;
import me.jobayeralmahmud.auth.repository.RoleRepository;
import me.jobayeralmahmud.auth.repository.UserRepository;
import me.jobayeralmahmud.auth.request.CreateUserRequest;
import me.jobayeralmahmud.auth.request.UpdateUserRequest;
import me.jobayeralmahmud.auth.response.UserDto;
import me.jobayeralmahmud.auth.service.UserService;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest request) {

        var user = request.toEntity(
                passwordEncoder.encode(request.password()),
                getRole(request.roleId())
        );

        var storedUser  = userRepository.save(user);
        triggerUserCreatedEvent(storedUser);

        return UserDto.toSingle(storedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.debug("Retrieving all users");
        return UserDto.toMultiple(userRepository.findAll());
    }

    @Override
    public User fetchUserById(UUID id) {
        log.debug("Retrieving user by ID: {}", id);
        return userRepository.findUserById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(
                        String.format("User not found with ID: %d", id)));
    }

    @Override
    public User fetchUserByEmail(String email) {
        log.debug("Retrieving user by email: {}", email);
        return fetchUserByEmailWithRoleAndPermission(email)
                .orElseThrow(() -> new ResourcesNotFoundException(
                        String.format("User not found with email: %s", email)));
    }

    @Override
    public Optional<User> fetchUserByEmailWithRoleAndPermission(String email) {
        return userRepository.findByEmailWithRoleAndPermissions(email);
    }

    @Override
    @Transactional
    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        var user = fetchUserById(id);

        user.update(request);

        log.info("User updated successfully with ID: {}", id);
        return UserDto.toSingle(userRepository.save(user));
    }

    @Override
    public boolean emailExists(String email) {
        log.debug("Checking if email exists: {}", email);
        return userRepository.existsByEmail(email);
    }

    private void triggerUserCreatedEvent(User user) {
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();

        eventPublisher.publishEvent(new UserCreatedEvent(this, user, baseUrl));
    }

    private Role getRole(Long roleId) {
        if (roleId == null) return null;
        return roleRepository.getReferenceById(roleId);
    }
}