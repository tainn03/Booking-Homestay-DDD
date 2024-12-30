package nnt.com.infrastructure.persistence.authentication.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.authentication.model.entity.Role;
import nnt.com.domain.authentication.repository.RoleDomainRepository;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.infrastructure.persistence.authentication.database.jpa.RoleInfraRepositoryJpa;
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
}
