package nnt.com.domain.aggregates.user.repository;

import nnt.com.domain.common.behaviors.BaseBehaviors;
import nnt.com.domain.aggregates.user.model.entity.Role;

public interface RoleDomainRepository extends BaseBehaviors<Role, String> {
    Role findByRoleName(String name);
}
