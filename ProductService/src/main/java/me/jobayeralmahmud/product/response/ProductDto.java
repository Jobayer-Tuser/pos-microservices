package me.jobayeralmahmud.product.response;

import me.jobayeralmahmud.product.entity.Category;
import me.jobayeralmahmud.product.entity.Product;

import java.util.List;
import java.util.Optional;
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
                getCategoryName(product),
                getVariants(product),
                getImages(product)

        );
    }

    public static List<ProductDto> fromEntity(List<Product> products) {
        return products.stream()
                .map(ProductDto::fromEntity)
                .toList();
    }

    private static String getCategoryName(Product product) {
        return Optional.ofNullable(product.getCategory())
                .map(Category::getName)
                .orElse(null);
    }

    private static List<ProductVariantDto> getVariants(Product product) {
        return product.getVariants().stream()
                .map(ProductVariantDto::fromEntity)
                .toList();
    }

    private static List<ProductImageDto> getImages(Product product) {
        return product.getImages().stream()
                .map(ProductImageDto::fromEntity)
                .toList();
    }
}