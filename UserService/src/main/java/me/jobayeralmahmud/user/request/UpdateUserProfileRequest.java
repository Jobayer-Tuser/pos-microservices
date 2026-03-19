package me.jobayeralmahmud.user.request;

import me.jobayeralmahmud.library.enums.Gender;

public record UpdateUserProfileRequest(
        String firstName,
        String lastName,
        String displayName,
        String phoneNumber,
        String permanentAddress,
        String permanentPostCode,
        String permanentCity,
        String permanentCountry,
        String invoiceAddress,
        String invoicePostCode,
        String invoiceCity,
        String invoiceCountry,
        int age,
        Gender gender
) {
}
