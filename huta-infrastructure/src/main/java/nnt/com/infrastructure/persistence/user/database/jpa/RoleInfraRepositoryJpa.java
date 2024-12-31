package nnt.com.infrastructure.persistence.user.database.jpa;

import jakarta.transaction.Transactional;
import nnt.com.domain.user.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleInfraRepositoryJpa extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String name);

    @Modifying
    @Transactional
    @Query("delete from Role r where r.role = ?1")
    void deleteByRole(String id);
}
