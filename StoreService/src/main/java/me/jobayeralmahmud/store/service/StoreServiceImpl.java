package me.jobayeralmahmud.store.service;

import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.response.StoreDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService{
    @Override
    public StoreDto createStore(StoreCreateRequest request) {
        return null;
    }

    @Override
    public StoreDto findStoreById(Long id) {
        return null;
    }

    @Override
    public StoreDto updateStore(Long id, StoreUpdateRequest request) {
        return null;
    }

    @Override
    public StoreDto getStoreByOwner() {
        return null;
    }

    @Override
    public List<StoreDto> findAllStores() {
        return List.of();
    }
}
