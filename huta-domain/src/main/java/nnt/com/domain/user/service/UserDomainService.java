package nnt.com.domain.user.service;

import nnt.com.domain.base.behaviors.BaseBehaviors;
import nnt.com.domain.user.model.entity.User;

public interface UserDomainService extends BaseBehaviors<User, Long> {
    User getByEmail(String emailOwner);
}
