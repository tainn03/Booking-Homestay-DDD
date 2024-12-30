package nnt.com.domain.homestay.service;

import nnt.com.domain.homestay.model.document.HomestaySearch;

import java.util.List;

public interface HomestaySearchDomainService {
    HomestaySearch save(HomestaySearch homestaySearch);

    List<HomestaySearch> findAll();

    void deleteById(Long id);

    List<HomestaySearch> searchByContent(String content);

    List<HomestaySearch> searchByLocation(double lat, double lon, int distance);
}
