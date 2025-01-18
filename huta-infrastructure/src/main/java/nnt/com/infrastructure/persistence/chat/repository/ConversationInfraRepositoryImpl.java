package nnt.com.infrastructure.persistence.chat.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.chat.model.entity.Conversation;
import nnt.com.domain.aggregates.chat.repository.ConversationDomainRepository;
import nnt.com.domain.common.exception.BusinessException;
import nnt.com.domain.common.exception.ErrorCode;
import nnt.com.infrastructure.persistence.chat.database.jpa.ConversationInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ConversationInfraRepositoryImpl implements ConversationDomainRepository {
    ConversationInfraRepositoryJpa conversationInfraRepositoryJpa;

    @Override
    public Conversation save(Conversation conversation) {
        return conversationInfraRepositoryJpa.save(conversation);
    }

    @Override
    public Conversation update(Conversation conversation) {
        return conversationInfraRepositoryJpa.save(conversation);
    }

    @Override
    public Conversation getById(Long id) {
        return conversationInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.CONVERSATION_NOT_FOUND));
    }

    @Override
    public Page<Conversation> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return conversationInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        conversationInfraRepositoryJpa.deleteById(id);
    }
}
