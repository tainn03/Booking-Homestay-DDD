package nnt.com.infrastructure.persistence.homestay.database.jpa;

import nnt.com.domain.aggregates.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingInfraRepositoryJpa extends JpaRepository<Review, Long> {
}
