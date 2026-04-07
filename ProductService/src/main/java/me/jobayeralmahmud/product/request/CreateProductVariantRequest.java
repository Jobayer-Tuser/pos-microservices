package me.jobayeralmahmud.product.request;

import me.jobayeralmahmud.product.entity.ProductVariant;
import me.jobayeralmahmud.product.enums.ProductStatus;

public record CreateProductVariantRequest(
        String variantName,
        String variantValue,
        double price,
        double sellPrice,
        int stockQuantity,
        ProductStatus status
) {
    public ProductVariant toEntity() {
        return ProductVariant.builder()
                .variantName(variantName)
                .variantValue(variantValue)
                .price(price)
                .sellPrice(sellPrice)
                .stockQuantity(stockQuantity)
                .status(status)
                .build();
    }
}