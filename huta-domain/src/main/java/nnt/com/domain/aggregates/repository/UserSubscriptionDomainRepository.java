package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.UserSubscription;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface UserSubscriptionDomainRepository extends BaseBehaviors<UserSubscription, Long> {
    List<UserSubscription> getByUser(Long id);
}
