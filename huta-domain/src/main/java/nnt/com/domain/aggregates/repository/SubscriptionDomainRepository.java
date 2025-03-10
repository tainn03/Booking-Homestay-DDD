package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Subscription;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface SubscriptionDomainRepository extends BaseBehaviors<Subscription, Long> {
    List<Subscription> getAll();
}
