package me.jobayeralmahmud.user.mapper;

import me.jobayeralmahmud.user.entity.UserProfile;
import me.jobayeralmahmud.user.request.CreateUserProfileRequest;
import me.jobayeralmahmud.user.request.UpdateUserProfileRequest;
import me.jobayeralmahmud.user.response.UserDto;
import me.jobayeralmahmud.user.response.UserProfileDto;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public UserProfile requestToEntity(CreateUserProfileRequest request, UserDto createdUser) {
        return UserProfile.builder()
                .userId(createdUser.id())
                .age(request.age())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .displayName(request.displayName())
                .phoneNumber(request.phoneNumber())
                .gender(request.gender())
                .build();
    }

    public UserProfileDto entityToDto(UserProfile profile) {
        return new UserProfileDto(
                profile.getId(),
                profile.getUserId(),
                profile.getAge(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getDisplayName(),
                profile.getPhoneNumber(),
                profile.getPermanentAddress(),
                profile.getPermanentPostCode(),
                profile.getPermanentCity(),
                profile.getPermanentCountry()
        );
    }

    public UserProfile updateProfile(UserProfile userProfile, UpdateUserProfileRequest request) {
        return userProfile.builder()
                .age(request.age())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .displayName(request.displayName())
                .phoneNumber(request.phoneNumber())
                .permanentAddress(request.permanentAddress())
                .permanentPostCode(request.permanentPostCode())
                .permanentCity(request.permanentCity())
                .permanentCountry(request.permanentCountry())
                .invoiceAddress(request.invoiceAddress())
                .invoicePostCode(request.invoicePostCode())
                .invoiceCity(request.invoiceCity())
                .invoiceCountry(request.invoiceCountry())
                .gender(request.gender())
                .build();
    }

    public UserProfileDto toSingleDto(UserProfile userProfile) {
        return new UserProfileDto(
                userProfile.getId(),
                userProfile.getUserId(),
                userProfile.getAge(),
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getDisplayName(),
                userProfile.getPhoneNumber(),
                userProfile.getPermanentAddress(),
                userProfile.getPermanentPostCode(),
                userProfile.getPermanentCity(),
                userProfile.getPermanentCountry()
        );
    }
}
