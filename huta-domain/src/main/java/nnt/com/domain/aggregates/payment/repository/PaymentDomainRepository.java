package nnt.com.domain.aggregates.payment.repository;

import nnt.com.domain.aggregates.payment.model.entity.Payment;
import nnt.com.domain.common.behaviors.BaseBehaviors;

public interface PaymentDomainRepository extends BaseBehaviors<Payment, Long> {
}
