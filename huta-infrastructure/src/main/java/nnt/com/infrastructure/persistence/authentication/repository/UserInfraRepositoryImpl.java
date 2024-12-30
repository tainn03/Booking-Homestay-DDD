package nnt.com.infrastructure.persistence.authentication.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.authentication.model.entity.User;
import nnt.com.domain.authentication.repository.UserDomainRepository;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.infrastructure.persistence.authentication.database.jpa.UserInfraRepositoryJpa;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserInfraRepositoryImpl implements UserDomainRepository {
    UserInfraRepositoryJpa userInfraRepository;

    @Override
    public boolean existsUserByEmail(String email) {
        return userInfraRepository.existsUserByEmail(email);
    }

    @Override
    public User save(User user) {
        return userInfraRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userInfraRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
