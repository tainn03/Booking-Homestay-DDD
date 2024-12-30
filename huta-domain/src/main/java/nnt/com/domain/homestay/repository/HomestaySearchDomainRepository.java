package nnt.com.domain.homestay.repository;

import nnt.com.domain.homestay.model.document.HomestaySearch;
import org.springframework.data.elasticsearch.core.query.Criteria;

import java.util.List;

public interface HomestaySearchDomainRepository {
    HomestaySearch save(HomestaySearch homestaySearch);

    List<HomestaySearch> findAll();

    void deleteById(Long id);

    List<HomestaySearch> search(Criteria criteria);
}
