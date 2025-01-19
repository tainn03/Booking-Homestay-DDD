package nnt.com.infrastructure.persistence.room.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Amenity;
import nnt.com.domain.aggregates.repository.AmenityDomainRepository;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.infrastructure.persistence.room.database.jpa.AmenityInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AmenityInfraRepositoryImpl implements AmenityDomainRepository {
    AmenityInfraRepositoryJpa amenityInfraRepositoryJpa;

    @Override
    public Amenity save(Amenity amenity) {
        return amenityInfraRepositoryJpa.save(amenity);
    }

    @Override
    public Amenity update(Amenity amenity) {
        return amenityInfraRepositoryJpa.save(amenity);
    }

    @Override
    public Amenity getById(String id) {
        return amenityInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.AMENITY_NOT_FOUND));
    }

    @Override
    public Page<Amenity> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return amenityInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        amenityInfraRepositoryJpa.deleteById(id);
    }
}
