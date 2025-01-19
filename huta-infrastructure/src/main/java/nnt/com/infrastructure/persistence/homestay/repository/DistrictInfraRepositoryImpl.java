package nnt.com.infrastructure.persistence.homestay.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.City;
import nnt.com.domain.aggregates.model.entity.District;
import nnt.com.domain.aggregates.repository.DistrictDomainRepository;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.infrastructure.persistence.homestay.database.jpa.DistrictInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DistrictInfraRepositoryImpl implements DistrictDomainRepository {
    DistrictInfraRepositoryJpa districtInfraRepositoryJpa;

    @Override
    public District findByNameAndCity(String districtName, City city) {
        return districtInfraRepositoryJpa.findByNameAndCity(districtName, city)
                .orElseThrow(() -> new BusinessException(ErrorCode.DISTRICT_NOT_FOUND));
    }

    @Override
    public District save(District district) {
        return districtInfraRepositoryJpa.save(district);
    }

    @Override
    public District update(District district) {
        return districtInfraRepositoryJpa.save(district);
    }

    @Override
    public District getById(Integer id) {
        return districtInfraRepositoryJpa.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DISTRICT_NOT_FOUND));
    }

    @Override
    public Page<District> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return districtInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Integer id) {
        districtInfraRepositoryJpa.deleteById(id);
    }
}
