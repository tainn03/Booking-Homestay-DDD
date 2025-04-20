package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.document.TripDocument;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface TripDocumentDomainService extends BaseBehaviors<TripDocument, String> {
    List<TripDocument> getAll();
}
