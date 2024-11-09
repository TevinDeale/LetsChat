package dev.fiinn.profile_service.controller;

import dev.fiinn.profile_service.dto.BaseResponseDto;
import dev.fiinn.profile_service.dto.CreateProfileDto;
import dev.fiinn.profile_service.dto.ProfileResponseDto;
import dev.fiinn.profile_service.dto.UpdateProfileDetailsDto;
import dev.fiinn.profile_service.enums.ProfileStatus;
import dev.fiinn.profile_service.model.Profile;
import dev.fiinn.profile_service.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles/")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Value("${profile.url.path}")
    private String apiUrl;

    @GetMapping("{profileId}")
    public ResponseEntity<Profile> getProfileById(@PathVariable(required = true) String profileId ) {
        return ResponseEntity.ok().body(profileService.findById(profileId));
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<Profile> getProfileByUserId(@PathVariable(required = true) UUID userId) {
        return ResponseEntity.ok().body(profileService.findByUserId(userId));
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<ProfileResponseDto>> getProfilesByStatus(@PathVariable(required = true) ProfileStatus status) {
        return ResponseEntity.ok().body(profileService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<Profile> createProfile(@Valid @RequestBody CreateProfileDto dto) {
        Profile profile = profileService.createProfile(dto);

        return ResponseEntity
                .created(URI.create(apiUrl+"api/profiles/"+profile.getProfileId()))
                .body(profile);
    }

    @PutMapping("{profileId}/{status}")
    public ResponseEntity<ProfileResponseDto> updateProfileStatus(
            @PathVariable String profileId,
            @PathVariable ProfileStatus status) {
        return ResponseEntity.accepted().body(profileService.updateProfileStatus(
                profileId, status
        ));
    }

    @PutMapping("update")
    public ResponseEntity<ProfileResponseDto> updateProfileDetails(@Valid @RequestBody UpdateProfileDetailsDto dto) {
        return ResponseEntity.accepted().body(profileService.updateProfileDetails(dto));
    }

    @DeleteMapping("{profileId}")
    public ResponseEntity<BaseResponseDto> deleteProfile(@PathVariable(required = true) String profileId) {
        return ResponseEntity.ok().body(profileService.deleteProfile(profileId));
    }
}
