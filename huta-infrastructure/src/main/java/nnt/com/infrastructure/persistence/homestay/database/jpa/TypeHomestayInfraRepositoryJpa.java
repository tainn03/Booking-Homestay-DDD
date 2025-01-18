package nnt.com.infrastructure.persistence.homestay.database.jpa;

import nnt.com.domain.aggregates.homestay.model.entity.TypeHomestay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeHomestayInfraRepositoryJpa extends JpaRepository<TypeHomestay, String> {
}
