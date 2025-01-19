package nnt.com.infrastructure.persistence.payment.database.jpa;

import nnt.com.domain.aggregates.model.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundInfraRepositoryJpa extends JpaRepository<Refund, Long> {
}
