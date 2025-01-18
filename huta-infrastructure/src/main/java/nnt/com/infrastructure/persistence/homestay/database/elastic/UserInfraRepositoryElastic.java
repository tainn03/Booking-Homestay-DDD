package nnt.com.infrastructure.persistence.homestay.database.elastic;

import nnt.com.domain.aggregates.user.model.document.UserDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfraRepositoryElastic extends ElasticsearchRepository<UserDocument, Long> {
}
