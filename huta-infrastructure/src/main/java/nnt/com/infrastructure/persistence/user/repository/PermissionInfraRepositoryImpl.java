package nnt.com.infrastructure.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.user.model.entity.Permission;
import nnt.com.domain.user.repository.PermissionDomainRepository;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.infrastructure.persistence.user.database.jpa.PermissionInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class PermissionInfraRepositoryImpl implements PermissionDomainRepository {
    PermissionInfraRepositoryJpa permissionInfraRepositoryJpa;

    @Override
    public Permission save(Permission permission) {
        return permissionInfraRepositoryJpa.save(permission);
    }

    @Override
    public Permission update(Permission permission) {
        return permissionInfraRepositoryJpa.save(permission);
    }

    @Override
    public Permission getById(String id) {
        return permissionInfraRepositoryJpa.findById(Long.parseLong(id))
                .orElseThrow(() -> new BusinessException(ErrorCode.PERMISSION_NOT_FOUND));
    }

    @Override
    public Page<Permission> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size).withSort(Sort.by(Sort.Direction.fromString(direction), sort));
        return permissionInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        permissionInfraRepositoryJpa.deleteById(Long.parseLong(id));
    }
}
