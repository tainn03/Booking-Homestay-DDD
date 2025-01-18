package nnt.com.infrastructure.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.common.exception.BusinessException;
import nnt.com.domain.common.exception.ErrorCode;
import nnt.com.domain.aggregates.user.model.entity.Token;
import nnt.com.domain.aggregates.user.repository.TokenDomainRepository;
import nnt.com.infrastructure.persistence.user.database.jpa.TokenInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TokenInfraRepositoryImpl implements TokenDomainRepository {
    TokenInfraRepositoryJpa tokenInfraRepositoryJpa;

    @Override
    public Token save(Token token) {
        return tokenInfraRepositoryJpa.save(token);
    }

    @Override
    public Token update(Token token) {
        return tokenInfraRepositoryJpa.save(token);
    }

    @Override
    public Token getById(Long id) {
        return tokenInfraRepositoryJpa.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.TOKEN_NOT_FOUND));
    }

    @Override
    public Page<Token> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return tokenInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        tokenInfraRepositoryJpa.deleteById(id);
    }

    @Override
    public void revokeTokensByUserId(Long userId) {
        tokenInfraRepositoryJpa.revokeTokensByUserId(userId);
    }
}
