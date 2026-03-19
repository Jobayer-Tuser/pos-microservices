package me.jobayeralmahmud.user.response;

import java.util.UUID;

public record UserProfileDto(
        Long id,
        UUID userId,
        int age,
        String firstName,
        String lastName,
        String displayName,
        String phoneNumber,
        String permanentAddress,
        String permanentPostCode,
        String permanentCity,
        String permanentCountry
) {
}
