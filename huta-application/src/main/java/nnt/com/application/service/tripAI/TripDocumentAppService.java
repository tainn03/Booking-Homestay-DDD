package nnt.com.application.service.tripAI;

import nnt.com.domain.aggregates.model.document.TripDocument;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface TripDocumentAppService extends BaseBehaviors<TripDocument, String> {
    List<TripDocument> getAll();
}
