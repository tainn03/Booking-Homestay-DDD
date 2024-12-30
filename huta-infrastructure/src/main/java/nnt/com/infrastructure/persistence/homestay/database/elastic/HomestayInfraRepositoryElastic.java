package nnt.com.infrastructure.persistence.homestay.database.elastic;

import nnt.com.domain.homestay.model.document.HomestaySearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface HomestayInfraRepositoryElastic extends ElasticsearchRepository<HomestaySearch, Long> {
}
