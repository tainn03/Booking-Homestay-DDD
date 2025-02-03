package nnt.com.infrastructure.persistence.homestay.database.jpa;

import nnt.com.domain.aggregates.model.entity.Homestay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomestayInfraRepositoryJpa extends JpaRepository<Homestay, Long> {
    @Query("SELECT h FROM Homestay h WHERE h.owner.id = ?1")
    List<Homestay> findByOwnerId(Long id);
}
