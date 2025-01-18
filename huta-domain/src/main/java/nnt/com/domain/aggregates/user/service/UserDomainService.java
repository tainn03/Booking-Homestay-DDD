package nnt.com.domain.aggregates.user.service;

import nnt.com.domain.common.behaviors.BaseBehaviors;
import nnt.com.domain.aggregates.user.model.entity.User;

public interface UserDomainService extends BaseBehaviors<User, Long> {
    User getByEmail(String emailOwner);
}
