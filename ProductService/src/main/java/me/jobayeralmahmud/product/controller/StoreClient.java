package me.jobayeralmahmud.product.controller;

import me.jobayeralmahmud.library.response.ApiResponse;
import me.jobayeralmahmud.product.response.StoreDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "store-service", url = "http://store-service:9014/dev/api/v1/store")
public interface StoreClient {

    @GetMapping("show/{id}")
    ResponseEntity<ApiResponse<StoreDto>> findStoreByStoreId(@PathVariable("id") UUID storeId);
}