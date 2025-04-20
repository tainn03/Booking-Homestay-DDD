package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.entity.RoomAvailable;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.time.LocalDate;
import java.util.List;

public interface RoomAvailableDomainService extends BaseBehaviors<RoomAvailable, Long> {
    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);

    List<LocalDate> getUnavailableDates(Long id, LocalDate of, LocalDate of1);
}
