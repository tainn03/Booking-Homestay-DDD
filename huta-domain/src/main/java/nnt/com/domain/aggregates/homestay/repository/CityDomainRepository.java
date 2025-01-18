package nnt.com.domain.aggregates.homestay.repository;

import nnt.com.domain.common.behaviors.BaseBehaviors;
import nnt.com.domain.aggregates.homestay.model.entity.City;

public interface CityDomainRepository extends BaseBehaviors<City, Integer> {
    City getByName(String name);
}
