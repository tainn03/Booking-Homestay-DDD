package nnt.com.infrastructure.persistence.room.database.jpa;

import jakarta.transaction.Transactional;
import nnt.com.domain.aggregates.model.entity.RoomAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomAvailableRepositoryJpa extends JpaRepository<RoomAvailable, Long> {
    // If exists any room available in the given date range, return false, otherwise return true
    @Query("SELECT CASE WHEN COUNT(ra) > 0 THEN FALSE ELSE TRUE END " +
            "FROM RoomAvailable ra " +
            "WHERE ra.room.id = ?1 AND ra.date BETWEEN ?2 AND ?3 AND ra.available = 0")
    boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut);

    @Query("DELETE FROM RoomAvailable ra " +
            "WHERE ra.room.id = ?1 AND ra.date BETWEEN ?2 AND ?3")
    @Modifying
    @Transactional
    void deleteByRoomIdAndDateBetween(Long roomId, LocalDate checkIn, LocalDate checkOut);


    @Query("SELECT ra.date " +
            "FROM RoomAvailable ra " +
            "WHERE ra.room.id = ?1 AND ra.date BETWEEN ?2 AND ?3 AND ra.available = 0")
    List<LocalDate> getUnavailableDates(Long id, LocalDate of, LocalDate of1);
}
