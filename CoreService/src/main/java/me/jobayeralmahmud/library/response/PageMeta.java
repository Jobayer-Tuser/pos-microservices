package me.jobayeralmahmud.library.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageMeta(
        Boolean hasNext,
        Integer pageSize,
        Long nextId,
        Long totalElements
) {}