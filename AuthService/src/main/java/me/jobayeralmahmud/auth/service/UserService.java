package me.jobayeralmahmud.auth.service;

import me.jobayeralmahmud.auth.entity.User;
import me.jobayeralmahmud.auth.request.CreateUserRequest;
import me.jobayeralmahmud.auth.request.UpdateUserRequest;
import me.jobayeralmahmud.auth.response.UserDto;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    /**
     * Creates a new user and triggers verification email process.
     *
     * @param request the user creation request
     * @return the created user DTO
     */
    UserDto createUser(CreateUserRequest request);

    /**
     * Retrieves all users.
     *
     * @return list of all user DTOs
     */
    List<UserDto> getAllUsers();

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID
     * @return the user DTO
     */
    User fetchUserById(UUID id);


    /**
     * Retrieves a user by email.
     *
     * @param email the user email
     * @return the user DTO
     * @throws ResourcesNotFoundException if user not found
     */
    User fetchUserByEmail(String email);

    /**
     * Fetch user by email with role and permissions
     *
     * @param email the user email
     * @return optional user value
     */
    Optional<User> fetchUserByEmailWithRoleAndPermission(String email);

    /**
     * Updates an existing user.
     *
     * @param id the user ID
     * @param request the update request
     * @return the updated user DTO
     * @throws ResourcesNotFoundException if user not found
     */
    UserDto updateUser(UUID id, UpdateUserRequest request);

    /**
     * Checks if an email already exists in the system.
     *
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean emailExists(String email);
}