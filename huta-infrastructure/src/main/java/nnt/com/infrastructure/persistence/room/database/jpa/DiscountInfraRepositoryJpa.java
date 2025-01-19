package nnt.com.infrastructure.persistence.room.database.jpa;

import nnt.com.domain.aggregates.model.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountInfraRepositoryJpa extends JpaRepository<Discount, Long> {
}
