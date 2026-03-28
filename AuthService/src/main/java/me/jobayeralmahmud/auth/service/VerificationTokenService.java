package me.jobayeralmahmud.auth.service;

import me.jobayeralmahmud.auth.entity.User;

import java.util.UUID;

public interface VerificationTokenService {
    /**
     * Store the verification token on user register
     *
     * @param user new user
     * @param token Jwt token
     */
    void addVerificationToken(User user, String token);

    /**
     * Verify the user and update the verification token status
     *
     * @param token Jwt token
     * @return long user id
     */
    UUID updateVerificationTokenStatus(String token);
}