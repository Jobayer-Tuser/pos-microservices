package me.jobayeralmahmud.store.controller;

import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.store.response.StoreDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class StoreController extends Controller{

    public ResponseEntity<ApiResponse<List<StoreDto>>> index() {
        return null;
    }

    public ResponseEntity<ApiResponse<List<StoreDto>>> store() {
        return null;
    }

    public ResponseEntity<ApiResponse<List<StoreDto>>> update() {
        return null;
    }

    public ResponseEntity<ApiResponse<List<StoreDto>>> show() {
        return null;
    }

    public ResponseEntity<ApiResponse<List<StoreDto>>> showStoreByAdmin() {
        return null;
    }
}
