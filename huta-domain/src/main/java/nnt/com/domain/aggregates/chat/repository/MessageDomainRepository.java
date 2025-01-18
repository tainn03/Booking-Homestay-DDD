package nnt.com.domain.aggregates.chat.repository;

import nnt.com.domain.aggregates.chat.model.entity.Message;
import nnt.com.domain.common.behaviors.BaseBehaviors;

public interface MessageDomainRepository extends BaseBehaviors<Message, Long> {
}
