package nnt.com.domain.user.repository;

import nnt.com.domain.user.model.entity.Token;
import nnt.com.domain.base.behaviors.BaseBehaviors;

public interface TokenDomainRepository extends BaseBehaviors<Token, Long> {
    void revokeTokensByUserId(Long userId);
}
