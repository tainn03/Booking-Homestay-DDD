package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface UserDomainService extends BaseBehaviors<User, Long> {
    User getByEmail(String emailOwner);

    UserResponse getProfile();
}
