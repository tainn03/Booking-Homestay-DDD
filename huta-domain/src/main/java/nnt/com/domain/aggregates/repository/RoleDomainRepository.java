package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Role;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface RoleDomainRepository extends BaseBehaviors<Role, String> {
    Role findByRoleName(String name);
}
