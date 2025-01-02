package nnt.com.domain.homestay.service;

import nnt.com.domain.homestay.model.entity.Homestay;

import java.util.List;

public interface RecommendationService {
    List<Homestay> recommendItemsForUser(Long userId);
}
