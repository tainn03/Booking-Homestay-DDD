package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface BookingDomainRepository extends BaseBehaviors<Booking, Long> {

    List<Booking> getByStatus(BookingStatus bookingStatus);

    Booking getByCode(String orderInfo);

    List<Booking> getByHomestayId(long homestayId);
}
