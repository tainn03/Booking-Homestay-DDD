package nnt.com.infrastructure.persistence.user.database.jpa;

import nnt.com.domain.aggregates.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInfraRepositoryJpa extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    boolean existsUserByEmail(String email);

    Optional<User> getByEmail(String emailOwner);

    @Query("SELECT u FROM User u WHERE u.role.role = ?1")
    List<User> findAllByRole(String roleType);
}
