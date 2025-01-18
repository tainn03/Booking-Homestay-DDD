package nnt.com.infrastructure.persistence.user.database.jpa;

import jakarta.transaction.Transactional;
import nnt.com.domain.aggregates.user.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenInfraRepositoryJpa extends JpaRepository<Token, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.revoked = true WHERE t.user.id = :userId AND t.expired = false AND t.revoked = false")
    void revokeTokensByUserId(long userId);

    Optional<Token> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expired = true OR t.revoked = true")
    void deleteInvalidTokens();
}
