package dev.fiinn.profile_service.repository;

import dev.fiinn.profile_service.enums.ProfileStatus;
import dev.fiinn.profile_service.model.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends MongoRepository<Profile, String> {
    Optional<Profile> findByUserId(UUID userId);
    Optional<Profile> findByUsername(String username);
    Optional<Profile> findByEmail(String email);
    List<Profile> findByStatus(ProfileStatus status);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
