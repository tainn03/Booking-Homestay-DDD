package nnt.com.domain.authentication.repository;

import nnt.com.domain.authentication.model.entity.Role;

public interface RoleDomainRepository {
    Role findByRoleName(String name);
}
