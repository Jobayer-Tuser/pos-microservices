package me.jobayeralmahmud.store.service;

import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.response.StoreDto;

import java.util.List;

public interface StoreService {
    StoreDto createStore(StoreCreateRequest request);
    StoreDto findStoreById(Long id);
    StoreDto updateStore(Long id, StoreUpdateRequest request);
    StoreDto getStoreByOwner();
    List<StoreDto> findAllStores();
}
