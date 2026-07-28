package me.jobayeralmahmud.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jobayeralmahmud.library.exceptions.ResourcesNotFoundException;
import me.jobayeralmahmud.library.response.CursorPageResponse;
import me.jobayeralmahmud.user.controller.AuthClient;
import me.jobayeralmahmud.user.entity.UserProfile;
import me.jobayeralmahmud.user.repository.UserProfileRepository;
import me.jobayeralmahmud.user.request.CreateUserProfileRequest;
import me.jobayeralmahmud.user.request.CreateUserRequest;
import me.jobayeralmahmud.user.request.UpdateUserProfileRequest;
import me.jobayeralmahmud.user.response.UserDto;
import me.jobayeralmahmud.user.response.UserProfileDto;
import me.jobayeralmahmud.user.response.UserProfileSummary;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileServiceImpl implements UserProfileService {

    private final AuthClient authClient;
    private final UserProfileRepository profileRepository;

    @Override
    @Transactional
    public UserProfileDto createUserProfile(CreateUserProfileRequest request) {
        var storedUser = createAndFetchAccountOrThrow(request);
        var userProfile = request.toEntity(storedUser);
        var savedProfile = profileRepository.save(userProfile);

        return UserProfileDto.fromEntity(savedProfile);
    }

    @Override
    @Transactional
    public UserProfileDto updateUserProfile(UUID id, UpdateUserProfileRequest request, UUID requesterId) {
        verifyProfileOwnership(id, requesterId);
        log.debug("Updating user profile with ID: {}", id);

        var existingProfile = findProfileByUserIdOrThrow(id);
        existingProfile.update(request);

        log.info("User profile updated successfully with ID: {}", id);
        return UserProfileDto.fromEntity(existingProfile);
    }

    @Override
    public CursorPageResponse<UserProfileSummary> collectUsers(String property, int pageSize, Long lastId) {
        log.debug("Retrieving users with cursor pagination - cursor: {}, size: {}",
                lastId, pageSize);

        var sort = Sort.by(Sort.Order.desc(property));
        var pageRequest = PageRequest.of(0, pageSize, sort);
        var userDetails = profileRepository.fetchNextPage(lastId, pageRequest);

        Long nextId = userDetails.hasNext() ? userDetails.getContent().getLast().id() : 0L;

        return new CursorPageResponse<>(userDetails.getContent(), pageSize, nextId, userDetails.hasNext());
    }

    private @NonNull UserDto createAndFetchAccountOrThrow(CreateUserProfileRequest request) {
        var createUserRequest = new CreateUserRequest(
                request.username(),
                request.email(),
                request.password(),
                request.roleId());

        var storedUser = authClient.createAccount(createUserRequest).data();

        if (storedUser == null) {
            throw new IllegalStateException("Failed to create user account from Auth Service.");
        }
        return storedUser;
    }

    private void verifyProfileOwnership(UUID profileId, UUID requesterId) {
        if (!profileId.equals(requesterId)) {
            throw new AuthorizationDeniedException("You are not authorized to update this profile data!");
        }
    }

    private @NonNull UserProfile findProfileByUserIdOrThrow(UUID id) {
        return profileRepository.findByUserId(id)
                .orElseThrow(() -> new ResourcesNotFoundException(
                        String.format("User profile not found with User ID: %s", id)));
    }
}