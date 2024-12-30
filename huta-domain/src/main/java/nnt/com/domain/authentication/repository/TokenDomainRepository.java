package nnt.com.domain.authentication.repository;

import nnt.com.domain.authentication.model.entity.Token;
import nnt.com.domain.base.behaviors.BaseBehaviors;

public interface TokenDomainRepository extends BaseBehaviors<Token, Long> {
    void revokeTokensByUserId(Long userId);
}
