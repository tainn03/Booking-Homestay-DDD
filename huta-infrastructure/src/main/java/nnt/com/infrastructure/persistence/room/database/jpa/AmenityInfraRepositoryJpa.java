package nnt.com.infrastructure.persistence.room.database.jpa;

import nnt.com.domain.room.model.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmenityInfraRepositoryJpa extends JpaRepository<Amenity, String> {
}
