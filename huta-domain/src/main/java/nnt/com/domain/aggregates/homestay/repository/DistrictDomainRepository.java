package nnt.com.domain.aggregates.homestay.repository;

import nnt.com.domain.common.behaviors.BaseBehaviors;
import nnt.com.domain.aggregates.homestay.model.entity.City;
import nnt.com.domain.aggregates.homestay.model.entity.District;

public interface DistrictDomainRepository extends BaseBehaviors<District, Integer> {
    District findByNameAndCity(String districtName, City city);
}
