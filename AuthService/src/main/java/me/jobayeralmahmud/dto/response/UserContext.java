package me.jobayeralmahmud.dto.response;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record UserContext(UUID id, String email, String role, List<String> permissions) implements Serializable {
}
