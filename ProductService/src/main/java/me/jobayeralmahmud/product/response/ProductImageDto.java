package me.jobayeralmahmud.product.response;

import me.jobayeralmahmud.product.entity.ProductImage;

public record ProductImageDto(
        String imageUrl
) { 
    public static ProductImageDto fromEntity(ProductImage image) {
        return new ProductImageDto(image.getImageUrl());
    }
}