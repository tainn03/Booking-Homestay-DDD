package nnt.com.domain.homestay.repository;

import nnt.com.domain.base.behaviors.BaseBehaviors;
import nnt.com.domain.homestay.model.entity.City;

public interface CityDomainRepository extends BaseBehaviors<City, Integer> {
    City getByName(String name);
}
