package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.dto.response.HomestayResponse;
import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.aggregates.model.entity.Review;
import nnt.com.domain.aggregates.repository.ReviewDomainRepository;
import nnt.com.domain.aggregates.service.HomestayDomainService;
import nnt.com.domain.aggregates.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RecommendationServiceImpl implements RecommendationService {
    ReviewDomainRepository reviewDomainRepository;
    HomestayDomainService homestayDomainService;

    @Override // Phương pháp dựa trên người dùng (User-Based Collaborative Filtering)
    public List<HomestayResponse> recommendUBCF(Long userId) {
        List<Review> allInteractions = reviewDomainRepository.findAll();
        Map<Long, Map<Long, Integer>> userHomestayMatrix = createUserHomestayMatrix(allInteractions);

        Map<Long, Double> similarityScores = new HashMap<>();
        Map<Long, Integer> currentUserHomestayRatings = userHomestayMatrix.get(userId);

        userHomestayMatrix.forEach((otherUserId, homestayRatings) -> {
            if (!otherUserId.equals(userId)) {
                double similarity = cosineSimilarityForUsers(currentUserHomestayRatings, homestayRatings);
                if (similarity > 0) { // Consider only positive similarities for recommendations
                    similarityScores.put(otherUserId, similarity);
                }
            }
        });

        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .flatMap(entry -> userHomestayMatrix.get(entry.getKey()).entrySet().stream())
                .filter(entry -> !currentUserHomestayRatings.containsKey(entry.getKey()))
                .map(Map.Entry::getKey)
                .distinct()
                .map(homestayDomainService::getHomestayById)
                .toList();
    }

    @Override // Phương pháp dựa trên mặt hàng (Item-Based Collaborative Filtering)
    public List<HomestayResponse> recommendIBCF(Long userId) {
        List<Review> allInteractions = reviewDomainRepository.findAll();
        Map<Long, Map<Long, Integer>> userHomestayMatrix = createUserHomestayMatrix(allInteractions);

        Map<Long, Integer> currentUserHomestayRatings = userHomestayMatrix.get(userId);

        Map<Long, Map<Long, Double>> itemSimilarityMatrix = new HashMap<>();
        Set<Long> homestayIds = userHomestayMatrix.values().stream()
                .flatMap(map -> map.keySet().stream())
                .collect(Collectors.toSet());

        homestayIds.forEach(homestayId -> {
            itemSimilarityMatrix.put(homestayId, new HashMap<>());
            homestayIds.forEach(otherHomestayId -> {
                if (!homestayId.equals(otherHomestayId)) {
                    double similarity = cosineSimilarityForItems(homestayId, otherHomestayId, userHomestayMatrix);
                    itemSimilarityMatrix.get(homestayId).put(otherHomestayId, similarity);
                }
            });
        });

        return currentUserHomestayRatings.keySet().stream()
                .flatMap(homestayId -> itemSimilarityMatrix.get(homestayId).entrySet().stream())
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .distinct()
                .filter(homestayId -> !currentUserHomestayRatings.containsKey(homestayId))
                .map(homestayDomainService::getHomestayById)
                .toList();
    }

    @Override // Phương pháp tính điểm phổ biến (Popularity Score)
    public List<HomestayResponse> recommendPopularity() {
        List<Review> allReviews = reviewDomainRepository.findAll();
        int totalReviews = allReviews.size();
        List<Homestay> popularHomestays = allReviews.stream()
                .collect(Collectors.groupingBy(Review::getHomestay,
                        Collectors.averagingDouble(review -> calculatePopularityScore(review.getHomestay(), totalReviews))))
                .entrySet().stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                .map(Map.Entry::getKey)
                .toList();
        return popularHomestays.stream()
                .map(homestay -> homestayDomainService.getHomestayById(homestay.getId()))
                .toList();
    }

    private double calculatePopularityScore(Homestay homestay, long totalReviews) {
        long numberOfReviews = homestay.getReviews().size();
        double averageRating = homestay.getReviews().stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        // Tính điểm phổ biến dựa trên số lượng và chất lượng đánh giá
        return (numberOfReviews / (double) totalReviews) * averageRating;
    }

    private Map<Long, Map<Long, Integer>> createUserHomestayMatrix(List<Review> allInteractions) {
        Map<Long, Map<Long, Integer>> userHomestayMatrix = new HashMap<>();
        allInteractions.forEach(interaction -> {
            userHomestayMatrix.computeIfAbsent(interaction.getUser().getId(), k -> new HashMap<>())
                    .put(interaction.getHomestay().getId(), interaction.getRating());
        });
        return userHomestayMatrix;
    }

    private double cosineSimilarityForUsers(Map<Long, Integer> userRatings, Map<Long, Integer> otherUserRatings) {
        if (userRatings == null || otherUserRatings == null) {
            return 0.0;
        }
        double dotProduct = 0;
        double normA = 0;
        double normB = 0;
        for (Long itemId : userRatings.keySet()) {
            dotProduct += userRatings.get(itemId) * otherUserRatings.getOrDefault(itemId, 0);
            normA += Math.pow(userRatings.get(itemId), 2);
            normB += Math.pow(otherUserRatings.getOrDefault(itemId, 0), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private double cosineSimilarityForItems(Long homestayId1, Long homestayId2, Map<Long, Map<Long, Integer>> userHomestayMatrix) {
        double dotProduct = 0;
        double normA = 0;
        double normB = 0;

        for (Map<Long, Integer> ratings : userHomestayMatrix.values()) {
            int rating1 = ratings.getOrDefault(homestayId1, 0);
            int rating2 = ratings.getOrDefault(homestayId2, 0);

            dotProduct += rating1 * rating2;
            normA += Math.pow(rating1, 2);
            normB += Math.pow(rating2, 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
