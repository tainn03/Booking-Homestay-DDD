package nnt.com.domain.aggregates.user.repository;

import nnt.com.domain.common.behaviors.BaseBehaviors;
import nnt.com.domain.aggregates.user.model.entity.Permission;

public interface PermissionDomainRepository extends BaseBehaviors<Permission, String> {
}
