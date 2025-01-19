package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Conversation;
import nnt.com.domain.aggregates.repository.ConversationDomainRepository;
import nnt.com.domain.aggregates.service.ConversationDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ConversationDomainServiceImpl implements ConversationDomainService {
    ConversationDomainRepository conversationDomainRepository;

    @Override
    public Conversation save(Conversation conversation) {
        return conversationDomainRepository.save(conversation);
    }

    @Override
    public Conversation update(Conversation conversation) {
        return conversationDomainRepository.save(conversation);
    }

    @Override
    public Conversation getById(Long id) {
        return conversationDomainRepository.getById(id);
    }

    @Override
    public Page<Conversation> getAll(int page, int size, String sort, String direction) {
        return conversationDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        conversationDomainRepository.delete(id);
    }
}
