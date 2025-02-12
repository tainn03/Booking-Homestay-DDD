package nnt.com.infrastructure.persistence.homestay.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.document.LocationDocument;
import nnt.com.domain.aggregates.repository.LocationSearchDomainRepository;
import nnt.com.infrastructure.persistence.homestay.database.elastic.LocationInfraRepositoryElastic;
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
public class LocationSearchInfraRepositoryImpl implements LocationSearchDomainRepository {
    LocationInfraRepositoryElastic locationInfraRepositoryElastic;
    ElasticsearchTemplate elasticsearchTemplate;


    @Override
    public LocationDocument save(LocationDocument locationDocument) {
        return locationInfraRepositoryElastic.save(locationDocument);
    }

    @Override
    public void delete(LocationDocument locationDocument) {
        locationInfraRepositoryElastic.delete(locationDocument);
    }

    @Override
    public List<LocationDocument> searchByAddress(String address) {
        String content = "*" + address + "*";
        Criteria criteria = new Criteria()
                .or(new Criteria("ward").expression(content))
                .or(new Criteria("district").expression(content))
                .or(new Criteria("city").expression(content));
        Query query = new CriteriaQuery(criteria);
        SearchHits<LocationDocument> searchHits = elasticsearchTemplate.search(query, LocationDocument.class);
        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationDocument> getAll() {
        return StreamSupport.stream(locationInfraRepositoryElastic.findAll().spliterator(), false).toList();
    }
}
