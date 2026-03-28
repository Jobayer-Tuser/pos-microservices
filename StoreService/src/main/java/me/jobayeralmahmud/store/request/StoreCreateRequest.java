package me.jobayeralmahmud.store.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record StoreCreateRequest(
        @NotBlank
        @org.hibernate.validator.constraints.UUID
        UUID owner_id,

        @NotBlank
        String brandName,
        String description,
        String email,

        @NotBlank
        String phoneNumber,
        String address,
        String storeType
) {
}
