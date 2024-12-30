package nnt.com.infrastructure.persistence.authentication.database.jpa;

import nnt.com.domain.authentication.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfraRepositoryJpa extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    boolean existsUserByEmail(String email);
}
