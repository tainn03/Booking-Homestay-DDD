package nnt.com.domain.homestay.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.homestay.model.entity.City;
import nnt.com.domain.homestay.model.entity.District;
import nnt.com.domain.homestay.repository.DistrictDomainRepository;
import nnt.com.domain.homestay.service.CityDomainService;
import nnt.com.domain.homestay.service.DistrictDomainService;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DistrictDomainServiceImpl implements DistrictDomainService {
    DistrictDomainRepository districtDomainRepository;
    CityDomainService cityDomainService;

    @Override
    public District getByName(String districtName, String cityName) {
        City city = cityDomainService.getByName(cityName);
        return districtDomainRepository.findByNameAndCity(districtName, city);
    }
}
