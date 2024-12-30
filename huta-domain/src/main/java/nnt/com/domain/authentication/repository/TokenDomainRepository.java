package nnt.com.domain.authentication.repository;

import nnt.com.domain.authentication.model.entity.Token;

public interface TokenDomainRepository {
    void save(Token token);

    void revokeTokensByUserId(Long userId);
}
