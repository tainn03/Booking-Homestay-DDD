package nnt.com.domain.aggregates.user.repository;

import nnt.com.domain.common.behaviors.BaseBehaviors;
import nnt.com.domain.aggregates.user.model.entity.User;

public interface UserDomainRepository extends BaseBehaviors<User, Long> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);

    User getByEmail(String emailOwner);
}
