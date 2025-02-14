package nnt.com.infrastructure.persistence.homestay.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.service.GHNDomainService;
import nnt.com.domain.shared.model.dto.District;
import nnt.com.domain.shared.model.dto.Province;
import nnt.com.domain.shared.model.dto.Ward;
import nnt.com.infrastructure.persistence.homestay.service.client.GHNApiClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class GHNInfraServiceImpl implements GHNDomainService {
    GHNApiClient ghnApiClient;

    @Override
    public List<Province> getProvinces() {
        List<Map<String, Object>> data = ghnApiClient.callApiGetProvinces();
        return data.stream()
                .map(item -> Province.builder()
                        .ProvinceID((Integer) item.get("ProvinceID"))
                        .ProvinceName((String) item.get("ProvinceName"))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<District> getDistricts(int provinceID) {
        List<Map<String, Object>> data = ghnApiClient.callApiGetDistricts(provinceID);
        return data.stream()
                .map(item -> District.builder()
                        .DistrictID((Integer) item.get("DistrictID"))
                        .DistrictName((String) item.get("DistrictName"))
                        .ProvinceID((Integer) item.get("ProvinceID"))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Ward> getWards(int districtID) {
        List<Map<String, Object>> data = ghnApiClient.callApiGetWards(districtID);
        return data.stream()
                .map(item -> Ward.builder()
                        .WardCode((String) item.get("WardCode"))
                        .WardName((String) item.get("WardName"))
                        .DistrictID((Integer) item.get("DistrictID"))
                        .build())
                .collect(Collectors.toList());
    }
}
