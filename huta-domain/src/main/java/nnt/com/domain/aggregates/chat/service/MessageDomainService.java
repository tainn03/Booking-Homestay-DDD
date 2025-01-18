package nnt.com.domain.aggregates.chat.service;

import nnt.com.domain.aggregates.chat.model.entity.Message;
import nnt.com.domain.common.behaviors.BaseBehaviors;

public interface MessageDomainService extends BaseBehaviors<Message, Long> {
}
