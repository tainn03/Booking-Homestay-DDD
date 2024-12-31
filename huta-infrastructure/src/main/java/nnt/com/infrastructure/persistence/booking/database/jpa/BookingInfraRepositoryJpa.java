package nnt.com.infrastructure.persistence.booking.database.jpa;

import nnt.com.domain.booking.model.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingInfraRepositoryJpa extends JpaRepository<Booking, Long> {
}
