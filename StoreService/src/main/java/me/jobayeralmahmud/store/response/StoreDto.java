package me.jobayeralmahmud.store.response;

import me.jobayeralmahmud.store.enums.StoreStatus;

import java.util.UUID;

public record StoreDto(
        Long id,
        UUID owner_id,
        String name,
        String phoneNumber,
        StoreStatus status
) {}
