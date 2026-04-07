package me.jobayeralmahmud.product.request;

import me.jobayeralmahmud.product.entity.Product;

import java.util.List;

public record CreateProductRequest(
        Long storeId,
        String name,
        String sku,
        String description,
        String imageUrl,
        String brand,
        Long categoryId,
        List<CreateProductVariantRequest> productVariants,
        List<CreateProductImageRequest> productImages
) {
    public Product toEntity() {
        return Product.builder()
                .storeId(storeId)
                .name(name)
                .sku(sku)
                .description(description)
                .imageUrl(imageUrl)
                .brand(brand)
                .build();
    }

}