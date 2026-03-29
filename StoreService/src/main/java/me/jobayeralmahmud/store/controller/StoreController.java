package me.jobayeralmahmud.store.controller;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.store.request.StoreCreateRequest;
import me.jobayeralmahmud.store.request.StoreUpdateRequest;
import me.jobayeralmahmud.store.service.StoreService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dev/api/v1/store")
public class StoreController extends Controller {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<?> index(Pageable pageable) {
        return ok(storeService.findAllStores(pageable), "Stores retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<?> store(@RequestBody StoreCreateRequest request) {
        return created(storeService.createStore(request), "Store created successfully");
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long storeId,  @RequestBody  StoreUpdateRequest request) throws AccessDeniedException {
        return ok(storeService.updateStore(storeId, request, currentUser()), "Store updated successfully");
    }

    @GetMapping("show/{id}")
    public ResponseEntity<?> show(@PathVariable("id") Long storeId) {
        return ok(storeService.findStoreById(storeId), "Store details retrieved successfully");
    }

    public ResponseEntity<?> showStoreByOwner() {
        return ok(storeService.getStoreByOwner(currentUser()), "Store details retrieved successfully");
    }
}
