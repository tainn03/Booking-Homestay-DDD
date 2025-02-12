package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.document.LocationDocument;

import java.util.List;

public interface LocationSearchDomainService {
    LocationDocument save(LocationDocument locationDocument);

    void delete(LocationDocument locationDocument);

    List<LocationDocument> searchByAddress(String address);

    List<LocationDocument> getAll();
}
