package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.entity.Message;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface MessageDomainService extends BaseBehaviors<Message, Long> {
}
