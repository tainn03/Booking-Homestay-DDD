package nnt.com.infrastructure.persistence.payment.database.jpa;

import nnt.com.domain.aggregates.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentInfraRepositoryJpa extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.booking.id = ?1")
    Optional<Payment> findByBookingId(long id);
}
