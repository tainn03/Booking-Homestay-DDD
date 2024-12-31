package nnt.com.domain.chat.repository;

import nnt.com.domain.base.behaviors.BaseBehaviors;
import nnt.com.domain.chat.model.entity.Message;

public interface MessageDomainRepository extends BaseBehaviors<Message, Long> {
}
