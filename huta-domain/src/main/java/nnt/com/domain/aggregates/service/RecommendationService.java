package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;

import java.util.List;

public interface RecommendationService {
    List<HomestayResponse> recommendHomestaysForUser(Long userId);
}
