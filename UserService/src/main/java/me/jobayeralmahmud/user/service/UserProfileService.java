package me.jobayeralmahmud.user.service;

import me.jobayeralmahmud.user.request.CreateUserProfileRequest;
import me.jobayeralmahmud.user.request.UpdateUserProfileRequest;
import me.jobayeralmahmud.user.response.UserProfileDto;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

public interface UserProfileService {

    UserProfileDto createUserProfile(CreateUserProfileRequest request);
    UserProfileDto updateUserProfile(UUID id, UpdateUserProfileRequest request, UUID requesterId) throws AccessDeniedException;
}
