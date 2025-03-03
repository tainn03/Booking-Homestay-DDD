package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.dto.request.UserUpdateRequest;
import nnt.com.domain.aggregates.model.dto.response.UserResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.model.mapper.UserMapper;
import nnt.com.domain.aggregates.repository.UserDomainRepository;
import nnt.com.domain.aggregates.service.ImageDomainService;
import nnt.com.domain.aggregates.service.UserDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
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
        if (email == null) {
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
        return userMapper.toDTO(getByEmail(email));
    }

    @Override
    public void updateAvatar(MultipartFile file) {
        User user = getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getAvatar() != null) {
            if (user.getAvatar().contains("cloudinary")) {
                imageDomainService.deleteFiles(List.of(user.getAvatar()));
            }
        }
        String url = imageDomainService.uploadFile(file);
        user.setAvatar(url);
        update(user);
    }

    @Override
    public UserResponse updateProfile(UserUpdateRequest request) {
        User user = getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        user = userMapper.updateEntity(request, user);
        return userMapper.toDTO(update(user));
    }

    @Override
    public void likeHomestay(User user, Homestay homestay) {
        user.getWishlist().stream()
                .filter(h -> h.getId().equals(homestay.getId()))
                .findFirst().ifPresentOrElse(
                        user.getWishlist()::remove,
                        () -> user.getWishlist().add(homestay)
                );
        update(user);
    }

    @Override
    public boolean checkLikedHomestay(User user, long homestayId) {
        return user.getWishlist().stream().anyMatch(h -> h.getId().equals(homestayId));
    }

    @Override
    public UserResponse getProfileById(Long userId) {
        return userMapper.toDTO(getById(userId));
    }
}
