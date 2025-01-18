package nnt.com.domain.aggregates.homestay.service;

import nnt.com.domain.aggregates.homestay.model.document.HomestayDocument;

import java.util.List;

public interface HomestaySearchDomainService {
    HomestayDocument save(HomestayDocument homestaySearch);

    List<HomestayDocument> findAll();

    void deleteById(Long id);

    List<HomestayDocument> searchByContent(String content);

    List<HomestayDocument> searchByLocation(double lat, double lon, int distance);
}
