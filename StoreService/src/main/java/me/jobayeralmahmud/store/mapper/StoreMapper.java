package me.jobayeralmahmud.store.mapper;

import me.jobayeralmahmud.store.entity.Store;
import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.response.StoreDto;
import org.springframework.stereotype.Service;

@Service
public class StoreMapper {
    public Store requestToEntity(StoreCreateRequest request) {
        return Store.builder()
                .ownerId(request.owner_id())
                .brandName(request.brandName())
                .description(request.description())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .storeType(request.storeType())
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
        store.setAddress(request.address());
        store.setStoreType(request.storeType());
        return store;
    }
}
