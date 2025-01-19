package nnt.com.infrastructure.persistence.homestay.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.City;
import nnt.com.domain.aggregates.repository.CityDomainRepository;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.infrastructure.persistence.homestay.database.jpa.CityInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CityInfraRepositoryImpl implements CityDomainRepository {
    CityInfraRepositoryJpa cityInfraRepositoryJpa;

    @Override
    public City getByName(String name) {
        return cityInfraRepositoryJpa.findByName(name)
                .orElseThrow(() -> new BusinessException(ErrorCode.CITY_NOT_FOUND));
    }

    @Override
    public City save(City city) {
        return cityInfraRepositoryJpa.save(city);
    }

    @Override
    public City update(City city) {
        return cityInfraRepositoryJpa.save(city);
    }

    @Override
    public City getById(Integer id) {
        return cityInfraRepositoryJpa.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CITY_NOT_FOUND));
    }

    @Override
    public Page<City> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return cityInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Integer id) {
        cityInfraRepositoryJpa.deleteById(id);
    }
}
