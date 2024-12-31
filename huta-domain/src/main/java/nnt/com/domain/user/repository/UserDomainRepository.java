package nnt.com.domain.user.repository;

import nnt.com.domain.user.model.entity.User;
import nnt.com.domain.base.behaviors.BaseBehaviors;

public interface UserDomainRepository extends BaseBehaviors<User, Long> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);
}
