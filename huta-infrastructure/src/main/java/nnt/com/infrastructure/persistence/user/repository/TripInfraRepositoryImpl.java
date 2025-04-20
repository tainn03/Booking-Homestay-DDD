package nnt.com.infrastructure.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.document.TripDocument;
import nnt.com.domain.aggregates.repository.TripDocumentDomainRepository;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.infrastructure.persistence.user.database.elastic.TripInfraRepositoryElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TripInfraRepositoryImpl implements TripDocumentDomainRepository {
    TripInfraRepositoryElastic repository;

    @Override
    public TripDocument save(TripDocument tripDocument) {
        return repository.save(tripDocument);
    }

    @Override
    public TripDocument update(TripDocument tripDocument) {
        return repository.save(tripDocument);
    }

    @Override
    public TripDocument getById(String id) {
        return repository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.TRIP_NOT_FOUND));
    }

    @Override
    public Page<TripDocument> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return repository.findAll(pageable);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<TripDocument> getAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
    }
}
