package nnt.com.domain.authentication.repository;

import nnt.com.domain.authentication.model.entity.Role;
import nnt.com.domain.base.behaviors.BaseBehaviors;

public interface RoleDomainRepository extends BaseBehaviors<Role, String> {
    Role findByRoleName(String name);
}
