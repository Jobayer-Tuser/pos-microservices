package me.jobayeralmahmud.library.response;

import org.springframework.data.domain.Slice;

public record CursorPageResponse<T>(
        Slice<T> data,
        int pageSize,
        Long nextId,
        boolean hasNext
) {
}