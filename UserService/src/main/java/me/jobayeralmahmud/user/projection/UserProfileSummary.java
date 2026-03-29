package me.jobayeralmahmud.user.entity;

import me.jobayeralmahmud.library.enums.Gender;

import java.util.UUID;

/**
 * Projection for {@link UserProfile}
 */
public record UserProfileInfo(
    Long id,
    UUID userId,
    int age,
    String firstName,
    String lastName,
    String displayName,
    String phoneNumber,
    String permanentAddress,
    Gender gender
) {}