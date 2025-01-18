package nnt.com.infrastructure.persistence.homestay.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.common.exception.BusinessException;
import nnt.com.domain.common.exception.ErrorCode;
import nnt.com.domain.aggregates.homestay.model.entity.Homestay;
import nnt.com.domain.aggregates.homestay.repository.HomestayDomainRepository;
import nnt.com.infrastructure.persistence.homestay.database.jpa.HomestayInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class HomestayInfraRepositoryImpl implements HomestayDomainRepository {
    HomestayInfraRepositoryJpa homestayInfraRepositoryJpa;

    @Override
    public Page<Homestay> getAll(int page, int size, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        return homestayInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public Homestay getById(Long homestayId) {
        return homestayInfraRepositoryJpa.findById(homestayId).orElseThrow(() -> new BusinessException(ErrorCode.HOMESTAY_NOT_FOUND));
    }

    @Override
    public Homestay save(Homestay homestay) {
        return homestayInfraRepositoryJpa.save(homestay);
    }

    @Override
    public void delete(Long homestayId) {
        homestayInfraRepositoryJpa.deleteById(homestayId);
    }

    @Override
    public Homestay update(Homestay homestay) {
        return homestayInfraRepositoryJpa.save(homestay);
    }
}
