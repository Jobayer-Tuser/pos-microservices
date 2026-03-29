package me.jobayeralmahmud.store.service;

import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.response.StoreDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public interface StoreService {
    StoreDto createStore(StoreCreateRequest request, UUID currentUser);
    StoreDto findStoreById(Long id);
    StoreDto updateStore(Long id, StoreUpdateRequest request, UUID userId) throws AccessDeniedException;
    StoreDto getStoreByOwner(UUID currentUser);
    Slice<StoreDto> findAllStores(Pageable pageable);
//    List<StoreDto> findAllStores(Pageable pageable);
}
