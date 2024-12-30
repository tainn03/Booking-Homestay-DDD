package nnt.com.domain.homestay.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.homestay.model.document.HomestaySearch;
import nnt.com.domain.homestay.repository.HomestaySearchDomainRepository;
import nnt.com.domain.homestay.service.HomestaySearchDomainService;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomestaySearchDomainServiceImpl implements HomestaySearchDomainService {
    HomestaySearchDomainRepository homestaySearchDomainRepository;

    @Override
    public HomestaySearch save(HomestaySearch homestaySearch) {
        return homestaySearchDomainRepository.save(homestaySearch);
    }

    @Override
    public List<HomestaySearch> findAll() {
        return homestaySearchDomainRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        homestaySearchDomainRepository.deleteById(id);
    }

    @Override
    public List<HomestaySearch> searchByContent(String content) {
        Criteria criteria = new Criteria()
                .or("name").contains(content)
                .or("description").contains(content)
                .or("addressDetail").contains(content);
        return homestaySearchDomainRepository.search(criteria);
    }

    @Override
    public List<HomestaySearch> searchByLocation(double lat, double lon, int distance) {
        GeoPoint centerPoint = new GeoPoint(lat, lon);
        Criteria criteria = new Criteria("location")
                .within(centerPoint, String.format("%dkm", distance));
        return homestaySearchDomainRepository.search(criteria);
    }
}
