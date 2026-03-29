package me.jobayeralmahmud.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.library.response.CursorPageResponse;
import me.jobayeralmahmud.user.controller.AuthClient;

import me.jobayeralmahmud.user.entity.UserProfile;
import me.jobayeralmahmud.user.entity.UserProfileInfo;
import me.jobayeralmahmud.user.mapper.UserProfileMapper;
import me.jobayeralmahmud.user.repository.UserProfileRepository;
import me.jobayeralmahmud.user.request.CreateUserProfileRequest;
import me.jobayeralmahmud.user.request.CreateUserRequest;
import me.jobayeralmahmud.user.request.GetUserProfileRequest;
import me.jobayeralmahmud.user.request.UpdateUserProfileRequest;
import me.jobayeralmahmud.user.response.UserDto;
import me.jobayeralmahmud.user.response.UserProfileDto;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final AuthClient authClient;
    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository profileRepository;

    @Override
    public UserProfileDto createUserProfile(CreateUserProfileRequest request) {

        var storedUser = getStoredUser(request);

        UserProfile userProfile = userProfileMapper.requestToEntity(request, storedUser);
        UserProfile save = profileRepository.save(userProfile);
        return userProfileMapper.entityToDto(save);
    }

    private @NonNull UserDto getStoredUser(CreateUserProfileRequest request) {
        var storedUser = authClient.createAccount(
                new CreateUserRequest(request.username(), request.email(), request.password(), request.roleId()))
                .data();

        if (storedUser == null) {
            throw new RuntimeException("Failed to create user account");
        }
        return storedUser;
    }

    @Override
    public UserProfileDto updateUserProfile(UUID id, UpdateUserProfileRequest request, UUID requesterId) throws AccessDeniedException {
        if (!id.equals(requesterId)) {
            throw new AccessDeniedException("You are not authorized to update this profile");
        }

        log.debug("Updating user with ID: {}", id);

        var userProfile = profileRepository.findByUserId(id)
                .orElseThrow(() -> new ResourcesNotFoundException(
                        String.format("User profile not found with User ID: %s", id)));

        var updateInfo = userProfileMapper.updateProfile(userProfile, request);
        var updatedUser = profileRepository.save(updateInfo);

        log.info("User updated successfully with ID: {}", id);
        return userProfileMapper.toSingleDto(updatedUser);
    }

    @Override
    public CursorPageResponse<UserProfileInfo> collectUsers(GetUserProfileRequest request) {
        log.debug("Retrieving users with cursor pagination - cursor: {}, size: {}",
                request.lastId(), request.pageSize());

        Sort sort = Sort.by(Sort.Order.desc(request.property()));
        PageRequest pageRequest = PageRequest.of(0, request.pageSize(), sort);
        List<UserProfileInfo> userDetails = profileRepository.fetchNextPage(request.lastId(), pageRequest);

        boolean hasNext = userDetails.size() == request.pageSize() ;
        Long nextId = hasNext ? userDetails.getLast().id() : 0L;

        return new CursorPageResponse<>(userDetails, request.pageSize(), nextId, hasNext);
    }
}
