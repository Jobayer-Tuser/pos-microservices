package me.jobayeralmahmud.product.request;

import me.jobayeralmahmud.product.entity.Product;
import me.jobayeralmahmud.product.enums.ProductStatus;

public record CreateProductVariantRequest(
        String variantName,
        String variantValue,
        double price,
        double sellPrice,
        int stockQuantity,
        ProductStatus status,
        Product product
) {
}