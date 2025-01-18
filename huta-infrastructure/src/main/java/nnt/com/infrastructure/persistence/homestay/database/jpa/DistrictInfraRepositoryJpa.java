package nnt.com.infrastructure.persistence.homestay.database.jpa;

import nnt.com.domain.aggregates.homestay.model.entity.City;
import nnt.com.domain.aggregates.homestay.model.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistrictInfraRepositoryJpa extends JpaRepository<District, Integer> {
    Optional<District> findByName(String districtName);

    Optional<District> findByNameAndCity(String districtName, City city);
}
