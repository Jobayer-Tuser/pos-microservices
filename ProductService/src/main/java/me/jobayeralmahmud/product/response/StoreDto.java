package me.jobayeralmahmud.product.response;

import me.jobayeralmahmud.product.enums.StoreStatus;

import java.util.UUID;

public record StoreDto (
        Long id,
        UUID owner_id,
        String name,
        String phoneNumber,
        StoreStatus status
) {}