package nnt.com.domain.aggregates.chat.service;

import nnt.com.domain.aggregates.chat.model.entity.Conversation;
import nnt.com.domain.common.behaviors.BaseBehaviors;

public interface ConversationDomainService extends BaseBehaviors<Conversation, Long> {
}
