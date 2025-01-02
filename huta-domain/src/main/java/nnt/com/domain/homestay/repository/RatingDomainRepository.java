package nnt.com.domain.homestay.repository;

import nnt.com.domain.base.behaviors.BaseBehaviors;
import nnt.com.domain.homestay.model.entity.Rating;

import java.util.List;

public interface RatingDomainRepository extends BaseBehaviors<Rating, Long> {
    List<Rating> findAll();
}
