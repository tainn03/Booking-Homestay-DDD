package nnt.com.application.service.search;

import nnt.com.domain.homestay.model.document.HomestaySearch;

import java.util.List;

public interface HomestaySearchAppService {
    List<HomestaySearch> searchByContent(String content);

    List<HomestaySearch> searchByLocation(String lat, String lon, int distance);
}
