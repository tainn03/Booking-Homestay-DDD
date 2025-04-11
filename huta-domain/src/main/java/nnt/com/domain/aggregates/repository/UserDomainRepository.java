package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface UserDomainRepository extends BaseBehaviors<User, Long> {
    boolean existsUserByEmail(String email);

    User findByEmail(String email);

    User getByEmail(String emailOwner);

    List<User> getByRole(String roleType);

    List<User> getAll();
}
