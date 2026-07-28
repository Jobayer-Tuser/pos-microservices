package me.jobayeralmahmud.user.response;

import me.jobayeralmahmud.user.entity.UserProfile;

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

    public static UserProfileDto fromEntity(UserProfile profile) {
        return new UserProfileDto(
                profile.getId(),
                profile.getUserId(),
                profile.getAge(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getDisplayName(),
                profile.getPhoneNumber(),
                profile.getPermanentAddress(),
                profile.getPermanentPostCode(),
                profile.getPermanentCity(),
                profile.getPermanentCountry()
        );
    }
}