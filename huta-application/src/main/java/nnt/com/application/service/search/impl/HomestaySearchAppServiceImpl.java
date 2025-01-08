package nnt.com.application.service.search.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.application.service.search.HomestaySearchAppService;
import nnt.com.domain.homestay.model.document.HomestayDocument;
import nnt.com.domain.homestay.service.HomestaySearchDomainService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class HomestaySearchAppServiceImpl implements HomestaySearchAppService {
    HomestaySearchDomainService homestaySearchDomainService;

    @Override
    public List<HomestayDocument> searchByContent(String content) {
        return homestaySearchDomainService.searchByContent(content);
    }

    @Override
    public List<HomestayDocument> searchByLocation(String lat, String lon, int distance) {
        return homestaySearchDomainService.searchByLocation(Double.parseDouble(lat), Double.parseDouble(lon), distance);
    }
}
