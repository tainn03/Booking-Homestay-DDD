package nnt.com.domain.homestay.repository;

import nnt.com.domain.base.behaviors.BaseBehaviors;
import nnt.com.domain.homestay.model.entity.City;
import nnt.com.domain.homestay.model.entity.District;

public interface DistrictDomainRepository extends BaseBehaviors<District, Integer> {
    District findByNameAndCity(String districtName, City city);
}
