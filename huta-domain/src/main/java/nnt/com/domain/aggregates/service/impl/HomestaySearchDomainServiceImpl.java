package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.document.HomestayDocument;
import nnt.com.domain.aggregates.model.dto.request.HomestayRequest;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.mapper.HomestaySearchMapper;
import nnt.com.domain.aggregates.repository.HomestaySearchDomainRepository;
import nnt.com.domain.aggregates.service.HomestaySearchDomainService;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomestaySearchDomainServiceImpl implements HomestaySearchDomainService {
    HomestaySearchDomainRepository homestaySearchDomainRepository;
    HomestaySearchMapper homestaySearchMapper;

    @Override
    public HomestayDocument save(HomestayRequest request, HomestayResponse response) {
        HomestayDocument document = homestaySearchMapper.toDocument(response);
        document.setLocation(request.getLocation());
        return save(document);
    }

    private HomestayDocument save(HomestayDocument homestaySearch) {
        return homestaySearchDomainRepository.save(homestaySearch);
    }

    @Override
    public List<HomestayDocument> findAll() {
        return homestaySearchDomainRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        homestaySearchDomainRepository.deleteById(id);
    }

    @Override
    public List<HomestayDocument> searchByContent(String content) {
        String query = "*" + content + "*";
        Criteria criteria = new Criteria()
                .or(new Criteria("name").expression(query))
                .or(new Criteria("description").expression(query))
                .or(new Criteria("typeHomestay").expression(query))
                .or(new Criteria("addressDetail").expression(query));
        return homestaySearchDomainRepository.search(criteria);
    }

    @Override
    public List<HomestayDocument> searchByLocation(double lat, double lon, int distance) {
        GeoPoint centerPoint = new GeoPoint(lat, lon);
        Criteria criteria = new Criteria("location")
                .within(centerPoint, String.format("%dkm", distance));
        return homestaySearchDomainRepository.search(criteria);
    }
}
