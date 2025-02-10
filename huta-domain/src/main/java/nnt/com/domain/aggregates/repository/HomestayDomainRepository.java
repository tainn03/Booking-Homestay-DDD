package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Homestay;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface HomestayDomainRepository extends BaseBehaviors<Homestay, Long> {
    List<Homestay> getByOwner(Long id);

    List<Homestay> getAll();
}
