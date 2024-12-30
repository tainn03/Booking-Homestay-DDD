package nnt.com.infrastructure.persistence.homestay.database.jpa;

import nnt.com.domain.homestay.model.entity.Homestay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomestayInfraRepositoryJpa extends JpaRepository<Homestay, Long> {
}
