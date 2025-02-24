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

import java.util.Comparator;
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

    @Override
    public HomestayDocument save(HomestayResponse response) {
        HomestayDocument document = homestaySearchMapper.toDocument(response);
        document.setLocation(new GeoPoint(response.getLat(), response.getLon()));
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
                .or(new Criteria("title").expression(query))
                .or(new Criteria("description").expression(query))
                .or(new Criteria("typeHomestay").expression(query))
                .or(new Criteria("address").expression(query));
        return homestaySearchDomainRepository.search(criteria);
    }

    @Override
    public List<HomestayDocument> searchByLocation(double lat, double lon, int distance) {
        GeoPoint centerPoint = new GeoPoint(lat, lon);
        Criteria criteria = new Criteria("location")
                .within(centerPoint, String.format("%dkm", distance));
        List<HomestayDocument> results = homestaySearchDomainRepository.search(criteria);
        results.sort(Comparator.comparingDouble(doc -> calculateDistance(doc.getLocation(), centerPoint)));
        return results;
    }

    private double calculateDistance(GeoPoint point1, GeoPoint point2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(point2.getLat() - point1.getLat());
        double lonDistance = Math.toRadians(point2.getLon() - point1.getLon());
        // Haversine Formula
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(point1.getLat())) * Math.cos(Math.toRadians(point2.getLat()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }
}
