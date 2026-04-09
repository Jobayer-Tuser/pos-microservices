package me.jobayeralmahmud.product.response;

import me.jobayeralmahmud.product.entity.Product;

import java.util.List;
import java.util.UUID;

public record ProductDto(
        UUID id,
        UUID storeId,
        String name,
        String description,
        String imageUrl,
        String brand,
        String category,
        List<ProductVariantDto> variants,
        List<ProductImageDto> images
) {
    public static ProductDto fromEntity(Product product) {
        return new ProductDto(
                product.getId(),
                product.getStoreId(),
                product.getName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getBrand(),
                product.getCategory() != null ? product.getCategory().getName() : null,
                product.getVariants().stream().map(ProductVariantDto::fromEntity).toList(),
                product.getImages().stream().map(ProductImageDto::fromEntity).toList()
        );
    }
}