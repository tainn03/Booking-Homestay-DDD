package nnt.com.domain.authentication.repository;

import nnt.com.domain.authentication.model.entity.User;
import nnt.com.domain.base.behaviors.BaseBehaviors;

public interface UserDomainRepository extends BaseBehaviors<User, Long> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);
}
