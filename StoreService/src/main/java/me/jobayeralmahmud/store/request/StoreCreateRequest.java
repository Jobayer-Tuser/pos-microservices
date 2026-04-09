package me.jobayeralmahmud.store.request;

import jakarta.validation.constraints.NotBlank;
import me.jobayeralmahmud.store.enums.StoreStatus;

import java.util.UUID;

public record StoreCreateRequest(
        @NotBlank
        UUID owner_id,

        @NotBlank
        String brandName,
        String email,

        @NotBlank
        String phoneNumber,
        String logoUrl,
        String currency,
        String timezone,
        Boolean isVerified,
        String verificationDocumentUrl,
        String description,
        String storeType,
        StoreStatus status
) {
}