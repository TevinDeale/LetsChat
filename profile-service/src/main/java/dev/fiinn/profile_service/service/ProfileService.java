package dev.fiinn.profile_service.service;

import dev.fiinn.profile_service.dto.BaseResponseDto;
import dev.fiinn.profile_service.dto.CreateProfileDto;
import dev.fiinn.profile_service.dto.ProfileResponseDto;
import dev.fiinn.profile_service.dto.UpdateProfileDetailsDto;
import dev.fiinn.profile_service.enums.ProfileStatus;
import dev.fiinn.profile_service.model.Profile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileService {
    Profile findById(String profileId);
    Profile findByUserId(UUID userId);
    Profile findByUsername(String username);
    ProfileResponseDto updateProfileStatus(String profileId, ProfileStatus status);
    Profile createProfile(CreateProfileDto dto);
    BaseResponseDto deleteProfile(String profileId);
    ProfileResponseDto changeUsername(UpdateProfileDetailsDto dto);
    ProfileResponseDto updateEmail(UpdateProfileDetailsDto dto);
    ProfileResponseDto updateFirstName(UpdateProfileDetailsDto dto);
    ProfileResponseDto updateLastName(UpdateProfileDetailsDto dto);
    ProfileResponseDto updateProfileDetails(UpdateProfileDetailsDto dto);
    List<ProfileResponseDto> findByStatus(ProfileStatus status);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
