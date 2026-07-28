package me.jobayeralmahmud.store.controller;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.library.annotations.ApiResponseMessage;
import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.response.StoreDto;
import me.jobayeralmahmud.store.service.StoreService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev/api/v1/store")
public class StoreController extends Controller {

    private final StoreService storeService;

    @GetMapping
    @ApiResponseMessage("Stores retrieved successfully")
    public Slice<StoreDto> index(Pageable pageable) {
        return storeService.findAllStores(pageable);
    }

    @PostMapping
    @ApiResponseMessage("Store created successfully")
    public StoreDto store(@RequestBody StoreCreateRequest request) {
        return storeService.createStore(request, currentUser());
    }

    @PatchMapping("/update/{id}")
    @ApiResponseMessage("Store updated successfully")
    public StoreDto update(@PathVariable("id") UUID storeId, @RequestBody StoreUpdateRequest request) {
        return storeService.updateStore(storeId, request, currentUser());
    }

    @GetMapping("/show/{id}")
    @ApiResponseMessage("Store details retrieved successfully")
    public StoreDto show(@PathVariable("id") UUID storeId) {
        return storeService.findStoreById(storeId);
    }

    @GetMapping("/find/owner")
    @ApiResponseMessage("Store details retrieved successfully")
    public StoreDto showStoreByOwner() {
        return storeService.getStoreByOwner(currentUser());
    }
}