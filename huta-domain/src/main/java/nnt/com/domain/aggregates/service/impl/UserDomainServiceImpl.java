package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.model.mapper.UserMapper;
import nnt.com.domain.aggregates.repository.UserDomainRepository;
import nnt.com.domain.aggregates.service.ImageDomainService;
import nnt.com.domain.aggregates.service.UserDomainService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserDomainServiceImpl implements UserDomainService {
    UserDomainRepository userDomainRepository;
    UserMapper userMapper;
    ImageDomainService imageDomainService;

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
        return userMapper.toDTO(getByEmail(email));
    }

    @Override
    public void updateAvatar(MultipartFile file) {
        User user = getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!user.getAvatar().contains("google")) {
            imageDomainService.deleteFiles(List.of(user.getAvatar()));
        }
        user.setAvatar(imageDomainService.uploadFile(file));
        update(user);
    }

    @Override
    public UserResponse updateProfile(UserUpdateRequest request) {
        User user = getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        user = userMapper.updateEntity(request, user);
        return userMapper.toDTO(update(user));
    }
}
