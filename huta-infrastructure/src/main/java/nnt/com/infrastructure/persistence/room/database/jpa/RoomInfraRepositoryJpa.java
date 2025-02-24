package nnt.com.infrastructure.persistence.room.database.jpa;

import nnt.com.domain.aggregates.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomInfraRepositoryJpa extends JpaRepository<Room, Long> {
    @Query("SELECT r FROM Room r WHERE r.homestay.id = ?1")
    List<Room> findAllByHomestayId(Long homestayId);
}
