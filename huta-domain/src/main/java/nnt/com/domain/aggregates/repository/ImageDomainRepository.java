package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Image;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface ImageDomainRepository extends BaseBehaviors<Image, String> {
}
