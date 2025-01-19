package nnt.com.infrastructure.persistence.user.database.jpa;

import nnt.com.domain.aggregates.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionInfraRepositoryJpa extends JpaRepository<Permission, Long> {
}
