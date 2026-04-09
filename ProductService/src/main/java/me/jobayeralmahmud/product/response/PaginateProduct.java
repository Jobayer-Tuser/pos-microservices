package me.jobayeralmahmud.product.response;

import java.util.List;
import java.util.UUID;

public record PaginateProduct<T>(
        List<T> data,
        int pageSize,
        UUID nextId,
        boolean hasNext
) {
}