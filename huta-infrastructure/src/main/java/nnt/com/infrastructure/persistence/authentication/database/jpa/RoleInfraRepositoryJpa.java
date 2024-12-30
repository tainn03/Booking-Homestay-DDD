package nnt.com.infrastructure.persistence.authentication.database.jpa;

import jakarta.transaction.Transactional;
import nnt.com.domain.authentication.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleInfraRepositoryJpa extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String name);

    @Modifying
    @Transactional
    @Query("delete from Role r where r.role = ?1")
    void deleteByRole(String id);
}
