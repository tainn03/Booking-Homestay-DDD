package nnt.com.infrastructure.persistence.homestay.database.jpa;

import nnt.com.domain.aggregates.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityInfraRepositoryJpa extends JpaRepository<City, Integer> {
    Optional<City> findByName(String name);
}
