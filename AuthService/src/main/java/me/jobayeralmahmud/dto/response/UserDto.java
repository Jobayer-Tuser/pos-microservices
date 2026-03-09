package me.jobayeralmahmud.dto.response;

import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        String role
) {}
