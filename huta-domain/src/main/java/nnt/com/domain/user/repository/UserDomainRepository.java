package nnt.com.domain.user.repository;

import nnt.com.domain.base.behaviors.BaseBehaviors;
import nnt.com.domain.user.model.entity.User;

public interface UserDomainRepository extends BaseBehaviors<User, Long> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);

    User getByEmail(String emailOwner);
}
