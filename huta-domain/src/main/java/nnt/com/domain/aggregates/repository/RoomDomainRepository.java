package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Room;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface RoomDomainRepository extends BaseBehaviors<Room, Long> {
    List<Room> getByHomestayId(Long homestayId);
}
