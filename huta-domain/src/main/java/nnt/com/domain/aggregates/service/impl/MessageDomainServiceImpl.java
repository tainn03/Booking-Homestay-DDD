package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.repository.MessageDomainRepository;
import nnt.com.domain.aggregates.service.MessageDomainService;
import nnt.com.domain.aggregates.model.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MessageDomainServiceImpl implements MessageDomainService {
    MessageDomainRepository messageDomainRepository;

    @Override
    public Message save(Message message) {
        return messageDomainRepository.save(message);
    }

    @Override
    public Message update(Message message) {
        return messageDomainRepository.save(message);
    }

    @Override
    public Message getById(Long id) {
        return messageDomainRepository.getById(id);
    }

    @Override
    public Page<Message> getAll(int page, int size, String sort, String direction) {
        return messageDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        messageDomainRepository.delete(id);
    }
}
