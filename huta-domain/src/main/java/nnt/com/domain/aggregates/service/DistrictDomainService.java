package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.entity.District;

public interface DistrictDomainService {
    District getByName(String districtName, String cityName);
}
