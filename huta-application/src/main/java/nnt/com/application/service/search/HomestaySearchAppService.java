package nnt.com.application.service.search;

import nnt.com.domain.aggregates.model.document.HomestayDocument;

import java.util.List;

public interface HomestaySearchAppService {
    List<HomestayDocument> searchByContent(String content);

    List<HomestayDocument> searchByLocation(String lat, String lon, int distance);
}
