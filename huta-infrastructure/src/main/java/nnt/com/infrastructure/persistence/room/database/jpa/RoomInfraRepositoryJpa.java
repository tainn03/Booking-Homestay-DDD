package nnt.com.infrastructure.persistence.room.database.jpa;

import nnt.com.domain.aggregates.room.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomInfraRepositoryJpa extends JpaRepository<Room, Long> {
}
