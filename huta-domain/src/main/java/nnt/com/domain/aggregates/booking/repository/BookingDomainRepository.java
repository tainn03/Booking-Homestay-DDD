package nnt.com.domain.aggregates.booking.repository;

import nnt.com.domain.aggregates.booking.model.entity.Booking;
import nnt.com.domain.common.behaviors.BaseBehaviors;

public interface BookingDomainRepository extends BaseBehaviors<Booking, Long> {
}
