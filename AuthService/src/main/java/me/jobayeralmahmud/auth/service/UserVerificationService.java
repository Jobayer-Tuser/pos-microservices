package me.jobayeralmahmud.auth.service;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.auth.entity.User;
import me.jobayeralmahmud.auth.jwt.JwtService;
import me.jobayeralmahmud.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service responsible for user email verification processes.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserVerificationService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;

    /**
     * Generates and stores a verification token for a newly created user.
     *
     * @param user the user to generate token for
     * @return the generated JWT token
     */
    @Transactional
    public String generateVerificationToken(User user) {
        log.debug("Generating verification token for user ID: {}", user.getId());

        String jwtToken = jwtService.generateAccessToken(user);
        verificationTokenService.addVerificationToken(user, jwtToken);

        log.info("Verification token generated for user ID: {}", user.getId());
        return jwtToken;
    }

    /**
     * Verifies a user's email using the provided verification token.
     *
     * @param token the verification token
     * @throws JwtException if token is invalid or expired
     * @throws UsernameNotFoundException if user not found
     */
    @Transactional
    public void verifyUserEmail(String token) {
        log.debug("Verifying user email with token");

        try {
            UUID userId = verificationTokenService.updateVerificationTokenStatus(token);

            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format("User not found with ID: %d", userId)));

            user.setEmailVerifiedAt(LocalDateTime.now());
            userRepository.save(user);

            log.info("Email verified successfully for user ID: {}", userId);

        } catch (UsernameNotFoundException e) {
            log.error("User not found during email verification", e);
            throw e;
        } catch (Exception e) {
            log.error("Error verifying user email", e);
            throw new JwtException("Failed to verify email: " + e.getMessage(), e);
        }
    }

    /**
     * Checks if a user's email is verified.
     *
     * @param userId the user ID
     * @return true if email is verified, false otherwise
     */
    public boolean isEmailVerified(UUID userId) {
        log.debug("Checking email verification status for user ID: {}", userId);

        return userRepository.findById(userId)
                .map(user -> user.getEmailVerifiedAt() != null)
                .orElse(false);
    }

    /**
     * Resends verification email for a user.
     *
     * @param userId the user ID
     * @return the new verification token
     * @throws UsernameNotFoundException if user not found
     */
    @Transactional
    public String resendVerificationEmail(UUID userId) {
        log.debug("Resending verification email for user ID: {}", userId);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found with ID: %d", userId)));

        // Invalidate old token and generate new one
        return generateVerificationToken(user);
    }
}