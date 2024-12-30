package nnt.com.infrastructure.persistence.authentication.database.jpa;

import jakarta.transaction.Transactional;
import nnt.com.domain.authentication.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TokenInfraRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false")
    List<Token> findAllValidTokenByUserId(String userId);

    Optional<Token> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expired = true")
    void deleteAllByExpirationBefore(Date date);
}
