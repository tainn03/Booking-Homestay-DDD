package nnt.com.domain.aggregates.homestay.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.homestay.repository.CityDomainRepository;
import nnt.com.domain.aggregates.homestay.model.entity.City;
import nnt.com.domain.aggregates.homestay.service.CityDomainService;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CityDomainServiceImpl implements CityDomainService {
    CityDomainRepository cityDomainRepository;

    @Override
    public City getByName(String name) {
        return cityDomainRepository.getByName(name);
    }
}
