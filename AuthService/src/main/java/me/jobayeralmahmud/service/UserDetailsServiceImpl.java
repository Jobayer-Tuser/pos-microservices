package me.jobayeralmahmud.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.repository.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Custom UserDetailsService implementation for Spring Security authentication.
 * This service is specifically for authentication purposes and should not be used
 * for general user management operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user details by username (email in this case) for Spring Security authentication.
     *
     * @param email the email (username) to search for
     * @return UserDetails object containing user and authority information
     * @throws UsernameNotFoundException if user with given email not found
     */
    @Override @NullMarked
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);

        return userRepository.findByEmailWithPermissions(email)
                .map(user -> {
                    log.debug("User found: {}", email);
                    return new SecuredUser(user);
                })
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new UsernameNotFoundException(
                            String.format("User not found with email: %s", email));
                });
    }
}