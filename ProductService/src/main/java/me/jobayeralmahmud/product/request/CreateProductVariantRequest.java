package me.jobayeralmahmud.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import me.jobayeralmahmud.product.entity.ProductVariant;
import me.jobayeralmahmud.product.enums.ProductStatus;

public record CreateProductVariantRequest(

        @NotBlank(message = "At least one Variant Name is required")
        String variantName,

        @NotBlank(message = "At least one Variant value is required")
        String variantValue,

        @NotNull(message = "Product Price is required")
        double price,
        double sellPrice,

        @NotNull(message = "Stock Quantity is required")
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