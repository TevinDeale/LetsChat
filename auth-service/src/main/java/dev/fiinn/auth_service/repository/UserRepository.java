package dev.fiinn.auth_service.repository;

import dev.fiinn.auth_service.model.Role;
import dev.fiinn.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndActiveTrue(String email);

    List<User> findByRolesContaining(Role role);

}
