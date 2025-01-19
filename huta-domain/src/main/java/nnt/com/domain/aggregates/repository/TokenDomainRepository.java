package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Token;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface TokenDomainRepository extends BaseBehaviors<Token, Long> {
    void revokeTokensByUserId(Long userId);
}
