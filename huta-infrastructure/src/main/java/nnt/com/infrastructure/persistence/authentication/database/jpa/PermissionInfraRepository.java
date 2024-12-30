package nnt.com.infrastructure.persistence.authentication.database.jpa;

import nnt.com.domain.authentication.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionInfraRepository extends JpaRepository<Permission, Long> {
}
