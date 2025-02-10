package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.document.HomestayDocument;
import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;

import java.util.List;

public interface HomestaySearchDomainService {
    HomestayDocument save(HomestayRequest request, HomestayResponse response);

    HomestayDocument save(HomestayResponse response);

    List<HomestayDocument> findAll();

    void deleteById(Long id);

    List<HomestayDocument> searchByContent(String content);

    List<HomestayDocument> searchByLocation(double lat, double lon, int distance);
}
