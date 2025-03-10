package nnt.com.infrastructure.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Subscription;
import nnt.com.domain.aggregates.repository.SubscriptionDomainRepository;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import nnt.com.infrastructure.persistence.user.database.jpa.SubscriptionInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SubscriptionInfraRepositoryImpl implements SubscriptionDomainRepository {
    SubscriptionInfraRepositoryJpa subscriptionInfraRepository;

    @Override
    public Subscription save(Subscription subscription) {
        return subscriptionInfraRepository.save(subscription);
    }

    @Override
    public Subscription update(Subscription subscription) {
        return subscriptionInfraRepository.save(subscription);
    }

    @Override
    public Subscription getById(Long id) {
        return subscriptionInfraRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    @Override
    public Page<Subscription> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return subscriptionInfraRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        subscriptionInfraRepository.deleteById(id);
    }

    @Override
    public List<Subscription> getAll() {
        return subscriptionInfraRepository.findAll();
    }
}
