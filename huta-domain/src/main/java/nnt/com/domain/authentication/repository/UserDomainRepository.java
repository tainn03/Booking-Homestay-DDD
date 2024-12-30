package nnt.com.domain.authentication.repository;

import nnt.com.domain.authentication.model.entity.User;

public interface UserDomainRepository {
    boolean existsUserByEmail(String email);

    User save(User user);

    User findByEmail(String email);
}
