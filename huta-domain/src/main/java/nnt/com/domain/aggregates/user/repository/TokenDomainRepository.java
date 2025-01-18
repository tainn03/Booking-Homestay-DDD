package nnt.com.domain.aggregates.user.repository;

import nnt.com.domain.common.behaviors.BaseBehaviors;
import nnt.com.domain.aggregates.user.model.entity.Token;

public interface TokenDomainRepository extends BaseBehaviors<Token, Long> {
    void revokeTokensByUserId(Long userId);
}
