package me.jobayeralmahmud.product.response;

import java.util.UUID;

public record CategoryDto(
        UUID id,
        String name,
        String slug
) {
}