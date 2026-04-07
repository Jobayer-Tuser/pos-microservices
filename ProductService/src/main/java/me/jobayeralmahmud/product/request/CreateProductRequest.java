package me.jobayeralmahmud.product.request;

import me.jobayeralmahmud.product.entity.Category;

public record CreateProductRequest(
        String name,
        String sku,
        String description,
        String imageUrl,
        String brand,
        Category category
) {
}