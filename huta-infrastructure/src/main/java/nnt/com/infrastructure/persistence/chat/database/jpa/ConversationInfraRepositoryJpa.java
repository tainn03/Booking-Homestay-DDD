package nnt.com.infrastructure.persistence.chat.database.jpa;

import nnt.com.domain.aggregates.chat.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationInfraRepositoryJpa extends JpaRepository<Conversation, Long> {
}
