package nnt.com.infrastructure.persistence.authentication.database.jpa;

import nnt.com.domain.authentication.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleInfraRepository extends JpaRepository<Role, Long> {
}
