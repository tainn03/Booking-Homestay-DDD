package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Conversation;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface ConversationDomainRepository extends BaseBehaviors<Conversation, Long> {
}
