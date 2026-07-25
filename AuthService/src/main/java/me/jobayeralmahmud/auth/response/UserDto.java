package me.jobayeralmahmud.auth.response;

import me.jobayeralmahmud.auth.entity.User;

import java.util.List;
import java.util.UUID;

public record UserDto(
        UUID id,
        String email
) {

    public static UserDto toSingle(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail()
        );
    }

    public static List<UserDto> toMultiple(List<User> users) {
        return users
                .stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getEmail()
                ))
                .toList();
    }
}