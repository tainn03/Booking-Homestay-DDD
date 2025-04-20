package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.document.TripDocument;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface TripDocumentDomainRepository extends BaseBehaviors<TripDocument, String> {
    List<TripDocument> getAll();
}
