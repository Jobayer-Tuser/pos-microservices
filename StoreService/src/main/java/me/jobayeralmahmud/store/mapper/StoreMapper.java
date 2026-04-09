package me.jobayeralmahmud.store.mapper;

import me.jobayeralmahmud.store.entity.Store;
import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.response.StoreDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StoreMapper {
    public Store requestToEntity(StoreCreateRequest request, UUID currentUser) {
        return Store.builder()
                .ownerId(currentUser)
                .brandName(request.brandName())
                .description(request.description())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .build();
    }

    public StoreDto entityToDto(Store store) {
        return new StoreDto(
                store.getId(),
                store.getOwnerId(),
                store.getBrandName(),
                store.getPhoneNumber(),
                store.getStatus()
        );
    }

    public Store updateStoreMap(Store store, StoreUpdateRequest request) {
        store.setBrandName(request.brandName());
        store.setDescription(request.description());
        store.setEmail(request.email());
        store.setPhoneNumber(request.phoneNumber());
        return store;
    }
}