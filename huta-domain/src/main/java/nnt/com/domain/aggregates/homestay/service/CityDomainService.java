package nnt.com.domain.aggregates.homestay.service;

import nnt.com.domain.aggregates.homestay.model.entity.City;

public interface CityDomainService {
    City getByName(String name);
}
