package me.jobayeralmahmud.service;

import me.jobayeralmahmud.dto.request.CreateUserRequest;
import me.jobayeralmahmud.dto.request.UpdateUserRequest;
import me.jobayeralmahmud.dto.response.UserDto;
import me.jobayeralmahmud.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * @throws if user not found
     */
    User getUserById(UUID id);


    /**
     * Retrieves a user by email.
     *
     * @param email the user email
     * @return the user DTO
     * @throws org.booking.exceptions.ResourcesNotFoundException if user not found
     */
    User getUserByEmail(String email);

    /**
     * Updates an existing user.
     *
     * @param id the user ID
     * @param request the update request
     * @return the updated user DTO
     * @throws org.booking.exceptions.ResourcesNotFoundException if user not found
     */
    @Transactional
    UserDto updateUser(UUID id, UpdateUserRequest request);

    /**
     * Checks if an email already exists in the system.
     *
     * @param email the email to check
     * @return true if email exists, false otherwise
     */
    boolean emailExists(String email);
}
