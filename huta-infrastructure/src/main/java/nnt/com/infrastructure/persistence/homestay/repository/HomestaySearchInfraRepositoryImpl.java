package nnt.com.infrastructure.persistence.homestay.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.homestay.model.document.HomestayDocument;
import nnt.com.domain.homestay.repository.HomestaySearchDomainRepository;
import nnt.com.infrastructure.persistence.homestay.database.elastic.HomestayInfraRepositoryElastic;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomestaySearchInfraRepositoryImpl implements HomestaySearchDomainRepository {
    HomestayInfraRepositoryElastic homestaySearchInfraRepository;
    ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public HomestayDocument save(HomestayDocument homestaySearch) {
        return homestaySearchInfraRepository.save(homestaySearch);
    }

    @Override
    public List<HomestayDocument> findAll() {
        return StreamSupport.stream(homestaySearchInfraRepository.findAll().spliterator(), false).toList();
    }

    @Override
    public void deleteById(Long id) {
        homestaySearchInfraRepository.deleteById(id);
    }

    @Override
    public List<HomestayDocument> search(Criteria criteria) {
        Query query = new CriteriaQuery(criteria);
        SearchHits<HomestayDocument> searchHits = elasticsearchTemplate.search(query, HomestayDocument.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
