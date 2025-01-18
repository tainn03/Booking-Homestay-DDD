package nnt.com.domain.aggregates.homestay.repository;

import nnt.com.domain.common.behaviors.BaseBehaviors;
import nnt.com.domain.aggregates.homestay.model.entity.Rating;

import java.util.List;

public interface RatingDomainRepository extends BaseBehaviors<Rating, Long> {
    List<Rating> findAll();
}
