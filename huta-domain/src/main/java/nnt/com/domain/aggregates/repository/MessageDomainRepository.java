package nnt.com.domain.aggregates.repository;

import nnt.com.domain.aggregates.model.entity.Message;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

public interface MessageDomainRepository extends BaseBehaviors<Message, Long> {
}
