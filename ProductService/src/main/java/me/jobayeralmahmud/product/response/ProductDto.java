package me.jobayeralmahmud.product.response;

public record ProductDto(
        Long id,
        String storeName,
        String name,
        String sku,
        String description,
        String imageUrl,
        String brand,
        String status,
        CategoryDto category
) {}