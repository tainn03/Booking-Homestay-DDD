package nnt.com.infrastructure.persistence.user.database.elastic;

import nnt.com.domain.aggregates.model.document.Action;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionInfraRepositoryElastic extends ElasticsearchRepository<Action, String> {
    // Custom query methods can be defined here if needed
}
