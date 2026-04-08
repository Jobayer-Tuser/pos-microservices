package me.jobayeralmahmud.product.response;

import me.jobayeralmahmud.product.enums.ProductStatus;

import me.jobayeralmahmud.product.entity.ProductVariant;

public record ProductVariantDto(
        String variantName,
        String variantValue,
        double price,
        double sellPrice,
        int stockQuantity,
        ProductStatus status
) {
    public static ProductVariantDto fromEntity(ProductVariant variant) {
        return new ProductVariantDto(
                variant.getVariantName(),
                variant.getVariantValue(),
                variant.getPrice(),
                variant.getSellPrice(),
                variant.getStockQuantity(),
                variant.getStatus()
        );
    }
}