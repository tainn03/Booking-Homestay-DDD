package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Payment;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface PaymentDomainRepository extends BaseBehaviors<Payment, Long> {
}
