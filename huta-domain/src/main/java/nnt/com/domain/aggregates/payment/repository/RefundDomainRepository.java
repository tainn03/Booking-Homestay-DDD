package nnt.com.domain.aggregates.payment.repository;

import nnt.com.domain.aggregates.payment.model.entity.Refund;
import nnt.com.domain.common.behaviors.BaseBehaviors;

public interface RefundDomainRepository extends BaseBehaviors<Refund, Long> {
}
