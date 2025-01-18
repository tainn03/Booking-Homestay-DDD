package nnt.com.domain.aggregates.homestay.service;

import nnt.com.domain.aggregates.homestay.model.entity.District;

public interface DistrictDomainService {
    District getByName(String districtName, String cityName);
}
