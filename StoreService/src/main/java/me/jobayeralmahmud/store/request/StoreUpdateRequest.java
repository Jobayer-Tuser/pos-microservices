package me.jobayeralmahmud.store.request;

import me.jobayeralmahmud.store.enums.StoreStatus;

public record StoreUpdateRequest(
        String brandName,
        String description,
        String email,
        String phoneNumber,
        String address,
        String storeType,
        StoreStatus status
) {
}
