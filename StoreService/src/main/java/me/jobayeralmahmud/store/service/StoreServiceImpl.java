package me.jobayeralmahmud.store.service;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.store.entity.Store;
import me.jobayeralmahmud.store.mapper.StoreMapper;
import me.jobayeralmahmud.store.repository.StoreRepository;
import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.response.StoreDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{

    private final StoreMapper storeMapper;
    private final StoreRepository storeRepository;

    @Override
    public StoreDto createStore(StoreCreateRequest request) {
        var store = storeRepository.save(storeMapper.requestToEntity(request));
        return storeMapper.entityToDto(store);
    }

    @Override
    public StoreDto findStoreById(Long id) {
        var store = findStoreByIdOrThrow(id);
        return storeMapper.entityToDto(store);
    }

    @Override
    public StoreDto updateStore(Long id, StoreUpdateRequest request, UUID currentUser) throws AccessDeniedException {
        var store = findStoreByIdOrThrow(id);

        if (!store.getOwnerId().equals(currentUser)) {
            throw new AccessDeniedException("You are not authorized to update this profile");
        }

        var updatedStore = storeRepository.save(storeMapper.updateStoreMap(store, request));
        return storeMapper.entityToDto(updatedStore);
    }

    @Override
    public StoreDto getStoreByOwner(UUID currentUser) {
        var store = storeRepository.findByOwnerId(currentUser)
                        .orElseThrow(() -> new ResourcesNotFoundException("Store not found for owner with id: " + currentUser));
        return storeMapper.entityToDto(store);
    }

    @Override
    public List<StoreDto> findAllStores(Pageable pageable) {
//        Sort sort = Sort.by(Sort.Direction.DESC, filterBy);
//        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return storeRepository.findAll(pageable).stream()
                .map(storeMapper::entityToDto)
                .toList();
    }

    private Store findStoreByIdOrThrow(Long id) {
        return storeRepository.findById(id)
                        .orElseThrow(() -> new ResourcesNotFoundException("Store not found with id: " + id));
    }
}
