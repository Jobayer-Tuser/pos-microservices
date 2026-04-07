package me.jobayeralmahmud.product.request;

import me.jobayeralmahmud.product.entity.ProductImage;

public record CreateProductImageRequest(
        String imageUrl,
        boolean isPrimary,
        int sortOrder
) {
    public ProductImage toEntity() {
        return ProductImage.builder()
                .imageUrl(imageUrl)
                .isPrimary(isPrimary)
                .sortOrder(sortOrder)
                .build();
    }
}