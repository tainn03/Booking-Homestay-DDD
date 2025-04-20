package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.document.TripDocument;
import nnt.com.domain.aggregates.repository.TripDocumentDomainRepository;
import nnt.com.domain.aggregates.service.TripDocumentDomainService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TripDocumentDomainServiceImpl implements TripDocumentDomainService {
    TripDocumentDomainRepository tripDocumentDomainRepository;

    @Override
    public List<TripDocument> getAll() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return tripDocumentDomainRepository.getAll().stream()
                .filter(tripDocument -> tripDocument.getUserEmail() != null && tripDocument.getUserEmail().equals(email))
                .toList();
    }

    @Override
    public Page<TripDocument> getAll(int page, int size, String sort, String direction) {
        return tripDocumentDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public TripDocument save(TripDocument tripDocument) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        tripDocument.setUserEmail(email);
        return tripDocumentDomainRepository.save(tripDocument);
    }

    @Override
    public TripDocument update(TripDocument tripDocument) {
        return tripDocumentDomainRepository.update(tripDocument);
    }

    @Override
    public TripDocument getById(String id) {
        return tripDocumentDomainRepository.getById(id);
    }

    @Override
    public void delete(String id) {
        tripDocumentDomainRepository.delete(id);
    }
}
