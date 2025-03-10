package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.UserSubscription;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface UserSubscriptionDomainRepository extends BaseBehaviors<UserSubscription, Long> {
}
