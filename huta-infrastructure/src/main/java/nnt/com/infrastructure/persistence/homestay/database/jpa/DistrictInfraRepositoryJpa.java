package nnt.com.infrastructure.persistence.homestay.database.jpa;

import nnt.com.domain.homestay.model.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictInfraRepositoryJpa extends JpaRepository<District, Integer> {
}
