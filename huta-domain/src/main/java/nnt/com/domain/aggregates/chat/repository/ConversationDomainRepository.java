package nnt.com.domain.aggregates.chat.repository;

import nnt.com.domain.aggregates.chat.model.entity.Conversation;
import nnt.com.domain.common.behaviors.BaseBehaviors;

public interface ConversationDomainRepository extends BaseBehaviors<Conversation, Long> {
}
