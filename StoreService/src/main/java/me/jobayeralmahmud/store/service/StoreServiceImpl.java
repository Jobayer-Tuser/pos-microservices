package me.jobayeralmahmud.store.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.store.entity.Store;
import me.jobayeralmahmud.store.mapper.StoreMapper;
import me.jobayeralmahmud.store.repository.StoreRepository;
import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.response.StoreDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreServiceImpl implements StoreService {

    private final StoreMapper storeMapper;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public StoreDto createStore(StoreCreateRequest request, UUID currentUser) {
        var store = storeMapper.requestToEntity(request, currentUser);
        return storeMapper.entityToDto(storeRepository.save(store));
    }

    @Override
    public StoreDto findStoreById(UUID id) {
        return storeMapper.entityToDto(findStoreByIdOrThrow(id));
    }

    @Override
    @Transactional
    public StoreDto updateStore(UUID id, StoreUpdateRequest request, UUID currentUser) {
        var store = findStoreByIdOrThrow(id);
        verifyStoreOwnership(store, currentUser);

        var updatedStore = storeMapper.updateStoreMap(store, request);
        return storeMapper.entityToDto(storeRepository.save(updatedStore));
    }

    @Override
    public StoreDto getStoreByOwner(UUID currentUser) {
        return storeRepository.findByOwnerId(currentUser)
                .map(storeMapper::entityToDto)
                .orElseThrow(() -> new ResourcesNotFoundException("Store not found for owner with id: " + currentUser));
    }

    @Override
    public Slice<StoreDto> findAllStores(Pageable pageable) {
        return storeRepository.findAllStore(pageable);
    }

    private Store findStoreByIdOrThrow(UUID id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException("Store not found with id: " + id));
    }

    private void verifyStoreOwnership(Store store, UUID currentUser) {
        if (!store.getOwnerId().equals(currentUser)) {
            throw new AuthorizationDeniedException("You are not owner of this store you can not update any data!");
        }
    }
}