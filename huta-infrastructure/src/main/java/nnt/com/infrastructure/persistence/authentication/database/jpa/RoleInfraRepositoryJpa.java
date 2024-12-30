package nnt.com.infrastructure.persistence.authentication.database.jpa;

import nnt.com.domain.authentication.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleInfraRepositoryJpa extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String name);
}
