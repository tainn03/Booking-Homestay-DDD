package nnt.com.infrastructure.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.domain.user.model.entity.User;
import nnt.com.domain.user.repository.UserDomainRepository;
import nnt.com.infrastructure.persistence.user.database.jpa.UserInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public User update(User user) {
        return userInfraRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        return userInfraRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public Page<User> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.fromString(direction), sort));
        return userInfraRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        userInfraRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userInfraRepository.findByEmail(email).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getByEmail(String emailOwner) {
        return userInfraRepository.getByEmail(emailOwner).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
