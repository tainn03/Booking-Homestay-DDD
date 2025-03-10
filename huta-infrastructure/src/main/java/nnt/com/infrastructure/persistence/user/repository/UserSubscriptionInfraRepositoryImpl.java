package nnt.com.infrastructure.persistence.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.UserSubscription;
import nnt.com.domain.aggregates.repository.UserSubscriptionDomainRepository;
import nnt.com.infrastructure.persistence.user.database.jpa.UserSubscriptionInfraRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserSubscriptionInfraRepositoryImpl implements UserSubscriptionDomainRepository {
    UserSubscriptionInfraRepositoryJpa userSubscriptionInfraRepository;

    @Override
    public UserSubscription save(UserSubscription userSubscription) {
        return userSubscriptionInfraRepository.save(userSubscription);
    }

    @Override
    public UserSubscription update(UserSubscription userSubscription) {
        return userSubscriptionInfraRepository.save(userSubscription);
    }

    @Override
    public UserSubscription getById(Long id) {
        return userSubscriptionInfraRepository.findById(id).orElse(null);
    }

    @Override
    public Page<UserSubscription> getAll(int page, int size, String sort, String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort));
        return userSubscriptionInfraRepository.findAll(pageable);
    }

    @Override
    public void delete(Long id) {
        userSubscriptionInfraRepository.deleteById(id);
    }
}
