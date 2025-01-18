package nnt.com.infrastructure.persistence.homestay.database.elastic;

import nnt.com.domain.aggregates.homestay.model.document.HomestayDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomestayInfraRepositoryElastic extends ElasticsearchRepository<HomestayDocument, Long> {
}
