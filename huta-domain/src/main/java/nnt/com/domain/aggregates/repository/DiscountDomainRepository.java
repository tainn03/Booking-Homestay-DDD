package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Discount;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface DiscountDomainRepository extends BaseBehaviors<Discount, Long> {
}
