package nnt.com.domain.homestay.repository;

import nnt.com.domain.homestay.model.document.HomestayDocument;
import org.springframework.data.elasticsearch.core.query.Criteria;

import java.util.List;

public interface HomestaySearchDomainRepository {
    HomestayDocument save(HomestayDocument homestaySearch);

    List<HomestayDocument> findAll();

    void deleteById(Long id);

    List<HomestayDocument> search(Criteria criteria);
}
