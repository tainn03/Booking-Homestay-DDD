package nnt.com.domain.aggregates.service;

import nnt.com.domain.shared.model.dto.District;
import nnt.com.domain.shared.model.dto.Province;
import nnt.com.domain.shared.model.dto.Ward;

import java.util.List;

public interface GHNDomainService {
    List<Province> getProvinces();

    List<District> getDistricts(int provinceID);

    List<Ward> getWards(int districtID);
}
