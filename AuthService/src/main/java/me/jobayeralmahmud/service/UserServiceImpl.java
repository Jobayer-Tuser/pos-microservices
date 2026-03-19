package me.jobayeralmahmud.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.dto.request.CreateUserRequest;
import me.jobayeralmahmud.dto.request.UpdateUserRequest;
import me.jobayeralmahmud.dto.response.UserDto;
import me.jobayeralmahmud.entity.Role;
import me.jobayeralmahmud.entity.User;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.mapper.UserMapper;
import me.jobayeralmahmud.repository.RoleRepository;
import me.jobayeralmahmud.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.UUID;

/**
 * Implementation of UserService focusing on core CRUD operations.
 * Email verification is handled by UserVerificationService.
 * Authentication is handled by CustomUserDetailsService.
 * Pagination queries are handled by UserQueryService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest request) {
        log.debug("Creating user with email: {}", request.email());
        Role role = null;

        if (request.roleId() != null ) {
           role = roleRepository.getReferenceById(request.roleId());
        }
        var user        = userMapper.toCreateEntity(request, role);
        var storedUser  = userRepository.save(user);

        // Capture base URL from current request context before async processing
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .build()
                .toUriString();

//        eventPublisher.publishEvent(new UserCreatedEvent(this, storedUser, baseUrl));

        log.info("User created successfully with ID: {}", storedUser.getId());
        return userMapper.toSingleDto(storedUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.debug("Retrieving all users");
        return userMapper.toMultipleDto(userRepository.findAll());
    }

    @Override
    public User getUserById(UUID id) {
        log.debug("Retrieving user by ID: {}", id);
        return userRepository.findUserById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(
                        String.format("User not found with ID: %d", id)));
    }

    @Override
    public User getUserByEmail(String email) {
        log.debug("Retrieving user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourcesNotFoundException(
                        String.format("User not found with email: %s", email)));
    }

    @Transactional
    @Override
    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        log.debug("Updating user with ID: {}", id);

        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(
                        String.format("User not found with ID: %d", id)));


        user.setEmail(request.email());

        var savedUser = userRepository.save(user);

        log.info("User updated successfully with ID: {}", id);
        return userMapper.toSingleDto(savedUser);
    }

    @Override
    public boolean emailExists(String email) {
        log.debug("Checking if email exists: {}", email);
        return userRepository.existsByEmail(email);
    }
}