package nnt.com.infrastructure.persistence.homestay.database.elastic;

import nnt.com.domain.aggregates.model.document.LocationDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationInfraRepositoryElastic extends ElasticsearchRepository<LocationDocument, String> {
}
