package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Permission;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface PermissionDomainRepository extends BaseBehaviors<Permission, String> {
}
