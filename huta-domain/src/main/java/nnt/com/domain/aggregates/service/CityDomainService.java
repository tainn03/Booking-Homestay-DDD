package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.entity.City;

public interface CityDomainService {
    City getByName(String name);
}
