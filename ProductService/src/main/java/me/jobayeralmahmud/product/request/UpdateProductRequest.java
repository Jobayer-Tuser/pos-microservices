package me.jobayeralmahmud.product.request;

import jakarta.validation.Valid;
import me.jobayeralmahmud.product.entity.Product;

import java.util.List;
import java.util.UUID;

public record UpdateProductRequest(
        UUID storeId,
        UUID categoryId,
        String name,
        String brand,
        String imageUrl,
        String description,
        Double basePrice,
        String baseSku,
        Integer baseStock,

        @Valid
        List<CreateProductVariantRequest> productVariants,

        @Valid
        List<CreateProductImageRequest> productImages
) {

    public Product toEntity() {
        return Product.builder()
                .storeId(storeId)
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .brand(brand)
                .build();
    }
}