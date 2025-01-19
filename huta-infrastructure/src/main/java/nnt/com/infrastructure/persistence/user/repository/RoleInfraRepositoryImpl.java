package nnt.com.infrastructure.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Role;
import nnt.com.domain.aggregates.repository.RoleDomainRepository;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.infrastructure.persistence.user.database.jpa.RoleInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoleInfraRepositoryImpl implements RoleDomainRepository {
    RoleInfraRepositoryJpa roleInfraRepositoryJpa;

    @Override
    public Role findByRoleName(String name) {
        return roleInfraRepositoryJpa.findByRole(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));
    }

    @Override
    public Role save(Role role) {
        return roleInfraRepositoryJpa.save(role);
    }

    @Override
    public Role update(Role role) {
        return roleInfraRepositoryJpa.save(role);
    }

    @Override
    public Role getById(String name) {
        return roleInfraRepositoryJpa.findByRole(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));
    }

    @Override
    public Page<Role> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return roleInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        roleInfraRepositoryJpa.deleteByRole(id);
    }
}
