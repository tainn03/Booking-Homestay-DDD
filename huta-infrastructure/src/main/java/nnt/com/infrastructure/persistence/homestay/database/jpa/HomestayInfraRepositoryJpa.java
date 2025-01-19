package nnt.com.infrastructure.persistence.homestay.database.jpa;

import nnt.com.domain.aggregates.model.entity.Homestay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomestayInfraRepositoryJpa extends JpaRepository<Homestay, Long> {
}
