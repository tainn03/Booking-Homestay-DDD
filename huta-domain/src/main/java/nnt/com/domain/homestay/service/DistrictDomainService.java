package nnt.com.domain.homestay.service;

import nnt.com.domain.homestay.model.entity.District;

public interface DistrictDomainService {
    District getByName(String districtName, String cityName);
}
