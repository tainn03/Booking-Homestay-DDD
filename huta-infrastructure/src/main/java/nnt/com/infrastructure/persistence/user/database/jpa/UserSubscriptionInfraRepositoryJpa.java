package nnt.com.infrastructure.persistence.user.database.jpa;

import nnt.com.domain.aggregates.model.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubscriptionInfraRepositoryJpa extends JpaRepository<UserSubscription, Long> {
    @Query("SELECT us FROM UserSubscription us WHERE us.user.id = ?1")
    List<UserSubscription> getByUser(Long id);
}
