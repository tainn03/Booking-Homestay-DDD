package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Review;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface ReviewDomainRepository extends BaseBehaviors<Review, Long> {
    List<Review> findAll();
}
