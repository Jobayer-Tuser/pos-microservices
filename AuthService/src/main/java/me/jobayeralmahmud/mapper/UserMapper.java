package me.jobayeralmahmud.mapper;

import lombok.RequiredArgsConstructor;
import me.jobayeralmahmud.dto.request.CreateUserRequest;
import me.jobayeralmahmud.dto.response.UserDto;
import me.jobayeralmahmud.entity.Role;
import me.jobayeralmahmud.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserMapper
{
    private final PasswordEncoder passwordEncoder;

    public User toCreateEntity(CreateUserRequest request, Role role)
    {
        return User.builder()
            .username(request.username())
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .role(role)
            .build();
    }

    public UserDto toSingleDto(User user)
    {
        return new UserDto(
            user.getId(),
            user.getEmail()
        );
    }

    public List<UserDto> toMultipleDto(List<User> users)
    {
        return users
            .stream()
            .map(user -> new UserDto(
                user.getId(),
                user.getEmail()
            ))
            .toList();
    }
}
