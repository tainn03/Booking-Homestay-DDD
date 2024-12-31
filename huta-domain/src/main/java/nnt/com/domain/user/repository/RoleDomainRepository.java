package nnt.com.domain.user.repository;

import nnt.com.domain.user.model.entity.Role;
import nnt.com.domain.base.behaviors.BaseBehaviors;

public interface RoleDomainRepository extends BaseBehaviors<Role, String> {
    Role findByRoleName(String name);
}
