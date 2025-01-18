package nnt.com.domain.aggregates.homestay.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.homestay.repository.RatingDomainRepository;
import nnt.com.domain.aggregates.homestay.service.RecommendationService;
import nnt.com.domain.aggregates.homestay.model.entity.Homestay;
import nnt.com.domain.aggregates.homestay.model.entity.Rating;
import nnt.com.domain.aggregates.homestay.service.HomestayDomainService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RecommendationServiceImpl implements RecommendationService {
    RatingDomainRepository ratingDomainRepository;
    HomestayDomainService homestayDomainService;

    @Override
    public List<Homestay> recommendItemsForUser(Long userId) {
        // Retrieve all interactions to build the user-item matrix
        List<Rating> allInteractions = ratingDomainRepository.findAll();
        Map<Long, Map<Long, Integer>> userHomestayMatrix = new HashMap<>();

        allInteractions.forEach(interaction -> {
            userHomestayMatrix.computeIfAbsent(interaction.getUser().getId(), k -> new HashMap<>())
                    .put(interaction.getHomestay().getId(), interaction.getRating());
        });

        // Calculate similarities with other users
        Map<Long, Double> similarityScores = new HashMap<>();
        Map<Long, Integer> currentUserHomestayRatings = userHomestayMatrix.get(userId);

        userHomestayMatrix.forEach((otherUserId, homestayRatings) -> {
            if (!otherUserId.equals(userId)) {
                double similarity = cosineSimilarity(currentUserHomestayRatings, homestayRatings);
                if (similarity > 0) { // Consider only positive similarities for recommendations
                    similarityScores.put(otherUserId, similarity);
                }
            }
        });

        // Collect item recommendations from similar users
        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .flatMap(entry -> userHomestayMatrix.get(entry.getKey()).entrySet().stream())
                .filter(entry -> !currentUserHomestayRatings.containsKey(entry.getKey()))
                .map(Map.Entry::getKey)
                .distinct()
                .map(homestayDomainService::getById)
                .collect(Collectors.toList());
    }

    private double cosineSimilarity(Map<Long, Integer> userRatings, Map<Long, Integer> otherUserRatings) {
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
}
