package nnt.com.infrastructure.persistence.chat.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.exception.BusinessException;
import nnt.com.domain.base.exception.ErrorCode;
import nnt.com.domain.chat.model.entity.Message;
import nnt.com.domain.chat.repository.MessageDomainRepository;
import nnt.com.infrastructure.persistence.chat.database.jpa.MessageInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MessageInfraRepositoryImpl implements MessageDomainRepository {
    MessageInfraRepositoryJpa messageInfraRepositoryJpa;

    @Override
    public Message save(Message message) {
        return messageInfraRepositoryJpa.save(message);
    }

    @Override
    public Message update(Message message) {
        return messageInfraRepositoryJpa.save(message);
    }

    @Override
    public Message getById(Long id) {
        return messageInfraRepositoryJpa.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.MESSAGE_NOT_FOUND));
    }

    @Override
    public Page<Message> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return messageInfraRepositoryJpa.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        messageInfraRepositoryJpa.deleteById(id);
    }
}
