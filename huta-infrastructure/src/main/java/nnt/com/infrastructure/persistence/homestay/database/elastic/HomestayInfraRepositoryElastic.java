package nnt.com.infrastructure.persistence.homestay.database.elastic;

import nnt.com.domain.homestay.model.document.HomestaySearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomestayInfraRepositoryElastic extends ElasticsearchRepository<HomestaySearch, Long> {
}
