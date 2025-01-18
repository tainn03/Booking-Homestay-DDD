package nnt.com.domain.aggregates.image.repository;

import nnt.com.domain.common.behaviors.BaseBehaviors;
import nnt.com.domain.aggregates.image.model.entity.Image;

public interface ImageDomainRepository extends BaseBehaviors<Image, String> {
}
