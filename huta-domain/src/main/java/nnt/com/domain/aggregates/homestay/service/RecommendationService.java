package nnt.com.domain.aggregates.homestay.service;

import nnt.com.domain.aggregates.homestay.model.entity.Homestay;

import java.util.List;

public interface RecommendationService {
    List<Homestay> recommendItemsForUser(Long userId);
}
