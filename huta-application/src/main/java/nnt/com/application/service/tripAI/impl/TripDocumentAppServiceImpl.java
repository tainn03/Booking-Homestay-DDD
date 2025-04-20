package nnt.com.application.service.tripAI.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.tripAI.TripDocumentAppService;
import nnt.com.domain.aggregates.model.document.TripDocument;
import nnt.com.domain.aggregates.service.TripDocumentDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TripDocumentAppServiceImpl implements TripDocumentAppService {
    TripDocumentDomainService tripDocumentDomainService;

    @Override
    public TripDocument save(TripDocument tripDocument) {
        return tripDocumentDomainService.save(tripDocument);
    }

    @Override
    public TripDocument update(TripDocument tripDocument) {
        return tripDocumentDomainService.update(tripDocument);
    }

    @Override
    public TripDocument getById(String id) {
        return tripDocumentDomainService.getById(id);
    }

    @Override
    public Page<TripDocument> getAll(int page, int size, String sort, String direction) {
        return tripDocumentDomainService.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(String id) {
        tripDocumentDomainService.delete(id);
    }

    @Override
    public List<TripDocument> getAll() {
        return tripDocumentDomainService.getAll();
    }
}
