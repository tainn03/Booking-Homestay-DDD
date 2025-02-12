package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.document.LocationDocument;

import java.util.List;

public interface LocationSearchDomainRepository {
    LocationDocument save(LocationDocument locationDocument);

    void delete(LocationDocument locationDocument);

    List<LocationDocument> searchByAddress(String address);

    List<LocationDocument> getAll();

}
