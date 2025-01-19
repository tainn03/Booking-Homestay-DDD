package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface BookingDomainRepository extends BaseBehaviors<Booking, Long> {
}
