package nnt.com.domain.aggregates.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.user.repository.UserDomainRepository;
import nnt.com.domain.aggregates.user.model.entity.User;
import nnt.com.domain.aggregates.user.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserDomainServiceImpl implements UserDomainService {
    UserDomainRepository userDomainRepository;

    @Override
    public User save(User user) {
        return userDomainRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userDomainRepository.update(user);
    }

    @Override
    public User getById(Long id) {
        return userDomainRepository.getById(id);
    }

    @Override
    public Page<User> getAll(int page, int size, String sort, String direction) {
        return userDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        userDomainRepository.delete(id);
    }

    @Override
    public User getByEmail(String emailOwner) {
        return userDomainRepository.getByEmail(emailOwner);
    }
}
