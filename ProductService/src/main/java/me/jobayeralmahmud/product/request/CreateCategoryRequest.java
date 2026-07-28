package me.jobayeralmahmud.product.request;

import jakarta.validation.constraints.NotBlank;
import me.jobayeralmahmud.product.entity.Category;

import java.util.UUID;

public record CreateCategoryRequest(
        @NotBlank
        String name,
        String description,
        UUID parentId
) {
        public Category toEntity() {
                return Category.builder()
                        .name(name)
                        .description(description)
                        .parentId(parentId)
                        .build();
        }
}