package nnt.com.domain.homestay.service;

import nnt.com.domain.homestay.model.entity.City;

public interface CityDomainService {
    City getByName(String name);
}
