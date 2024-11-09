package dev.fiinn.profile_service.service;

import dev.fiinn.profile_service.dto.BaseResponseDto;
import dev.fiinn.profile_service.dto.CreateProfileDto;
import dev.fiinn.profile_service.dto.ProfileResponseDto;
import dev.fiinn.profile_service.dto.UpdateProfileDetailsDto;
import dev.fiinn.profile_service.enums.ProfileStatus;
import dev.fiinn.profile_service.exception.*;
import dev.fiinn.profile_service.model.Profile;
import dev.fiinn.profile_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService{

    private final ProfileRepository profileRepository;

    @Override
    public Profile findById(String profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException(
                        "Profile with ID: " + profileId + " does not exist"
                ));
    }

    @Override
    public Profile findByUserId(UUID userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException(
                        "Profile with userId: " + userId + " was not found"
                ));
    }

    @Override
    public Profile findByUsername(String username) {
        return profileRepository.findByUsername(username)
                .orElseThrow(() -> new ProfileNotFoundException(
                        "Profile with username: " + username + " was not found"
                ));
    }

    @Override
    @Transactional
    public ProfileResponseDto updateProfileStatus(String profileId, ProfileStatus status) {

        if (!(status.equals(ProfileStatus.OFFLINE) || status.equals(ProfileStatus.ONLINE)) ) {
            throw new ProfileStatusUpdateException("Status: " + status + " does not exist");
        }

        Profile profile = findById(profileId);

        profile.updateStatus(status);

        profileRepository.save(profile);

        return buildProfileResponse(profile);
    }

    @Override
    @Transactional
    public Profile createProfile(CreateProfileDto dto) {

        if (existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username: " + dto.getUsername() + " already exists");
        }

        if (dto.getEmail() != null && existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email: " + dto.getEmail() + " is already in use");
        }
        Profile createProfile = Profile.builder()
                .userId(dto.getUserId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build();

        return profileRepository.save(createProfile);
    }

    @Override
    @Transactional
    public BaseResponseDto deleteProfile(String profileId) {
        Profile profile = findById(profileId);

        profileRepository.delete(profile);

        return BaseResponseDto.builder()
                .message("Profile with ID: " + profileId + "was deleted successfully")
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public ProfileResponseDto changeUsername(UpdateProfileDetailsDto dto) {
        Profile profile = findById(dto.getProfileId());

        if (profile.getUsername().equals(dto.getUsername())) {
            throw new InvalidProfileDataException("The username is the same. No update made");
        }

        if (existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username: " + dto.getUsername() + " is already in use");
        }

        profile.setUsername(dto.getUsername());
        profileRepository.save(profile);

        return buildProfileResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponseDto updateEmail(UpdateProfileDetailsDto dto) {
        Profile profile = findById(dto.getProfileId());

        if (profile.getEmail().equals(dto.getEmail())) {
            throw new InvalidProfileDataException("The email is the same. No update made");
        }

        if (existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Email: " + dto.getEmail() + " is already in use");
        }

        profile.setEmail(dto.getEmail());
        profileRepository.save(profile);

        return buildProfileResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponseDto updateFirstName(UpdateProfileDetailsDto dto) {
        Profile profile = findById(dto.getProfileId());

        String firstName = dto.getFirstName();

        if (firstName.isBlank() || firstName.equals(profile.getFirstName())) {
            if (firstName.isBlank()) {
                throw new InvalidProfileDataException("First name cannot be blank");
            }

            throw new InvalidProfileDataException("Profile already has first name: " + firstName);
        }

        profile.setFirstName(firstName);

        profileRepository.save(profile);

        return buildProfileResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponseDto updateLastName(UpdateProfileDetailsDto dto) {

        Profile profile = findById(dto.getProfileId());

        String lastName = dto.getLastName();

        if (lastName.isBlank() || lastName.equals(profile.getLastName())) {
            if (lastName.isBlank()) {
                throw new InvalidProfileDataException("Last name cannot be blank");
            }

            throw new InvalidProfileDataException("Profile already has last name: " + lastName);
        }

        profile.setLastName(lastName);

        profileRepository.save(profile);

        return buildProfileResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponseDto updateProfileDetails(UpdateProfileDetailsDto dto) {

        Profile profile = findById(dto.getProfileId());

        validateUpdates(profile, dto);
        updateFields(profile, dto);

        return buildProfileResponse(profileRepository.save(profile));
    }

    @Override
    public List<ProfileResponseDto> findByStatus(ProfileStatus status) {
        List<Profile> profiles = profileRepository.findByStatus(status);

        return profiles.stream().map(this::buildProfileResponse).toList();
    }

    @Override
    public boolean existsByUsername(String username) {
        return profileRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return profileRepository.existsByEmail(email);
    }

    private ProfileResponseDto buildProfileResponse(Profile profile) {
        return ProfileResponseDto.builder()
                .profileId(profile.getProfileId())
                .userId(profile.getUserId())
                .email(profile.getEmail())
                .status(profile.getStatus())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .statusLastUpdated(profile.getStatusLastUpdated())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    private void updateFields(Profile profile, UpdateProfileDetailsDto dto) {
        if (dto.getUsername() != null) {
            profile.setUsername(dto.getUsername());
        }

        if (dto.getEmail() != null) {
            profile.setEmail(dto.getEmail());
        }

        if (dto.getFirstName() != null) {
            profile.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            profile.setLastName(dto.getLastName());
        }
    }

    private void validateUpdates(Profile profile, UpdateProfileDetailsDto dto) {

        String firstName = dto.getFirstName();
        String lastName = dto.getLastName();

        if (dto.getUsername() != null) {
            if (profile.getUsername().equals(dto.getUsername())) {
                throw new InvalidProfileDataException("The username is the same. No update made");
            }

            if (existsByUsername(dto.getUsername())) {
                throw new DuplicateResourceException("Username: " + dto.getUsername() + " is already in use");
            }
        }

        if (dto.getEmail() != null) {
            if (profile.getEmail().equals(dto.getEmail())) {
                throw new InvalidProfileDataException("The email is the same. No update made");
            }

            if (existsByEmail(dto.getEmail())) {
                throw new DuplicateResourceException("Email: " + dto.getEmail() + " is already in use");
            }
        }

        if (firstName != null) {
            if (firstName.isBlank() || firstName.equals(profile.getFirstName())) {
                if (firstName.isBlank()) {
                    throw new InvalidProfileDataException("First name cannot be blank");
                }

                throw new InvalidProfileDataException("Profile already has first name: " + firstName);
            }
        }

        if (lastName != null) {
            if (lastName.isBlank() || lastName.equals(profile.getLastName())) {
                if (lastName.isBlank()) {
                    throw new InvalidProfileDataException("Last name cannot be blank");
                }

                throw new InvalidProfileDataException("Profile already has last name: " + lastName);
            }
        }
    }
}
