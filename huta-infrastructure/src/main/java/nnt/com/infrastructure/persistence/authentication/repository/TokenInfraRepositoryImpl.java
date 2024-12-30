package nnt.com.infrastructure.persistence.authentication.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.authentication.model.entity.Token;
import nnt.com.domain.authentication.repository.TokenDomainRepository;
import nnt.com.infrastructure.persistence.authentication.database.jpa.TokenInfraRepositoryJpa;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TokenInfraRepositoryImpl implements TokenDomainRepository {
    TokenInfraRepositoryJpa tokenInfraRepositoryJpa;

    @Override
    public void save(Token token) {
        tokenInfraRepositoryJpa.save(token);
    }

    @Override
    public void revokeTokensByUserId(Long userId) {
        tokenInfraRepositoryJpa.revokeTokensByUserId(userId);
    }
}
