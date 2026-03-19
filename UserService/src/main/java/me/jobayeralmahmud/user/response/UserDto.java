package me.jobayeralmahmud.user.response;

import java.util.UUID;

public record UserDto(
        UUID id,
        String email
) {
}
