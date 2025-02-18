package nnt.com.infrastructure.persistence.booking.database.jpa;

import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingInfraRepositoryJpa extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.status = ?1")
    List<Booking> findByStatus(BookingStatus bookingStatus);
}
