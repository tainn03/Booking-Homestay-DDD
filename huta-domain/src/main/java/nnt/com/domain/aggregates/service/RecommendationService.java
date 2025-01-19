package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.entity.Homestay;

import java.util.List;

public interface RecommendationService {
    List<Homestay> recommendItemsForUser(Long userId);
}
