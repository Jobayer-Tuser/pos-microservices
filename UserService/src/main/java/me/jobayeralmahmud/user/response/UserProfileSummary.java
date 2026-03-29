package me.jobayeralmahmud.user.response;

import me.jobayeralmahmud.library.enums.Gender;
import me.jobayeralmahmud.user.entity.UserProfile;

import java.util.UUID;

/**
 * Projection for {@link UserProfile}
 */
public record UserProfileSummary(
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