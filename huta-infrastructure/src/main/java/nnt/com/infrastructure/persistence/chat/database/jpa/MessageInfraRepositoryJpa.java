package nnt.com.infrastructure.persistence.chat.database.jpa;

import nnt.com.domain.chat.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageInfraRepositoryJpa extends JpaRepository<Message, Long> {
}
