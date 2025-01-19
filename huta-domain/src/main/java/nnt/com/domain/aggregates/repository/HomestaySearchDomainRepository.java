package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.document.HomestayDocument;
import org.springframework.data.elasticsearch.core.query.Criteria;

import java.util.List;

public interface HomestaySearchDomainRepository {
    HomestayDocument save(HomestayDocument homestaySearch);

    List<HomestayDocument> findAll();

    void deleteById(Long id);

    List<HomestayDocument> search(Criteria criteria);
}
