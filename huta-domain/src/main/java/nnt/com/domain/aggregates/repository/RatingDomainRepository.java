package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Rating;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface RatingDomainRepository extends BaseBehaviors<Rating, Long> {
    List<Rating> findAll();
}
