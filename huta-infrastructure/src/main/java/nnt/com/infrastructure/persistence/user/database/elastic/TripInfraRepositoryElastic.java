package nnt.com.infrastructure.persistence.user.database.elastic;

import nnt.com.domain.aggregates.model.document.TripDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripInfraRepositoryElastic extends ElasticsearchRepository<TripDocument, String> {
}
