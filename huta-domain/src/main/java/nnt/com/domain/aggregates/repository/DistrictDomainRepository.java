package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.City;
import nnt.com.domain.aggregates.model.entity.District;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface DistrictDomainRepository extends BaseBehaviors<District, Integer> {
    District findByNameAndCity(String districtName, City city);
}
