package me.jobayeralmahmud.library.response;

import java.util.List;

public record CursorPageResponse<T>(
        List<T> data,
        int pageSize,
        Long nextId,
        boolean hasNext
) {}