package nnt.com.domain.aggregates.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.document.LocationDocument;
import nnt.com.domain.aggregates.repository.LocationSearchDomainRepository;
import nnt.com.domain.aggregates.service.LocationSearchDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LocationSearchDomainServiceImpl implements LocationSearchDomainService {
    LocationSearchDomainRepository locationSearchDomainRepository;

    @Override
    public LocationDocument save(LocationDocument locationDocument) {
        return locationSearchDomainRepository.save(locationDocument);
    }

    @Override
    public void delete(LocationDocument locationDocument) {
        locationSearchDomainRepository.delete(locationDocument);
    }

    @Override
    public List<LocationDocument> searchByAddress(String address) {
        return locationSearchDomainRepository.searchByAddress(address);
    }

    @Override
    public List<LocationDocument> getAll() {
        return locationSearchDomainRepository.getAll();
    }
}
