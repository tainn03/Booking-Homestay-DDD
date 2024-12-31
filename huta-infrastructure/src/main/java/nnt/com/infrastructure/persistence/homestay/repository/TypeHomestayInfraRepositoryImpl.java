package nnt.com.infrastructure.persistence.homestay.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.domain.homestay.model.entity.TypeHomestay;
import nnt.com.domain.homestay.repository.TypeHomestayDomainRepository;
import nnt.com.infrastructure.persistence.homestay.database.jpa.TypeHomestayInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TypeHomestayInfraRepositoryImpl implements TypeHomestayDomainRepository {
    TypeHomestayInfraRepositoryJpa typeHomestayInfraRepositoryJpa;

    @Override
    public TypeHomestay save(TypeHomestay typeHomestay) {
        return typeHomestayInfraRepositoryJpa.save(typeHomestay);
    }

    @Override
    public TypeHomestay update(TypeHomestay typeHomestay) {
        return typeHomestayInfraRepositoryJpa.save(typeHomestay);
    }

    @Override
    public TypeHomestay getById(String id) {
        return typeHomestayInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.TYPE_HOMESTAY_NOT_FOUND));
    }

    @Override
    public Page<TypeHomestay> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return typeHomestayInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        typeHomestayInfraRepositoryJpa.deleteById(id);
    }
}
