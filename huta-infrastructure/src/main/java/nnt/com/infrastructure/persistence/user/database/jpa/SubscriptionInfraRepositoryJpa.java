package nnt.com.infrastructure.persistence.user.database.jpa;

import nnt.com.domain.aggregates.model.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionInfraRepositoryJpa extends JpaRepository<Subscription, Long> {
}
