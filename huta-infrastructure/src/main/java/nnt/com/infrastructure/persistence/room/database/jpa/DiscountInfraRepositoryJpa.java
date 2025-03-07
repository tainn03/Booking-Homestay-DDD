package nnt.com.infrastructure.persistence.room.database.jpa;

import jakarta.transaction.Transactional;
import nnt.com.domain.aggregates.model.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountInfraRepositoryJpa extends JpaRepository<Discount, Long> {
    @Modifying
    @Query("DELETE FROM Discount d WHERE d.id = :id")
    @Transactional
    void deleteById(@Param("id") Long id);
}
