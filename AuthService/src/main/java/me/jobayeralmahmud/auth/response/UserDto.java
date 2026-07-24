package me.jobayeralmahmud.auth.dto.response;

import java.util.UUID;

public record UserDto(
        UUID id,
        String email
) {}
