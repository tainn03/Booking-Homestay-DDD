package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.model.mapper.UserMapper;
import nnt.com.domain.aggregates.repository.UserDomainRepository;
import nnt.com.domain.aggregates.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserDomainServiceImpl implements UserDomainService {
    UserDomainRepository userDomainRepository;
    UserMapper userMapper;

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

    @Override
    public UserResponse getProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return email == null ? null : userMapper.toDTO(getByEmail(email));
    }
}
