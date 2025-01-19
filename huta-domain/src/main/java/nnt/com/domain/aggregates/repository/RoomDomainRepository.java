package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Room;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface RoomDomainRepository extends BaseBehaviors<Room, Long> {
}
