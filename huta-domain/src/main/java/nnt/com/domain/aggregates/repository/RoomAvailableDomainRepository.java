package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.RoomAvailable;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.time.LocalDate;
import java.util.List;

public interface RoomAvailableDomainRepository extends BaseBehaviors<RoomAvailable, Long> {
    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);

    List<LocalDate> getUnavailableDates(Long id, LocalDate of, LocalDate of1);
}
