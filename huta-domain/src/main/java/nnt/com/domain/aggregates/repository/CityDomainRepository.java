package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.City;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface CityDomainRepository extends BaseBehaviors<City, Integer> {
    City getByName(String name);
}
