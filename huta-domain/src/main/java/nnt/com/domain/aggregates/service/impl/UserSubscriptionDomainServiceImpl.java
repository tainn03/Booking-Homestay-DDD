package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.UserSubscription;
import nnt.com.domain.aggregates.repository.UserSubscriptionDomainRepository;
import nnt.com.domain.aggregates.service.UserSubscriptionDomainService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserSubscriptionDomainServiceImpl implements UserSubscriptionDomainService {
    UserSubscriptionDomainRepository userSubscriptionDomainRepository;

    @Override
    public UserSubscription save(UserSubscription userSubscription) {
        return userSubscriptionDomainRepository.save(userSubscription);
    }

    @Override
    public UserSubscription update(UserSubscription userSubscription) {
        return userSubscriptionDomainRepository.update(userSubscription);
    }

    @Override
    public UserSubscription getById(Long id) {
        return userSubscriptionDomainRepository.getById(id);
    }

    @Override
    public Page<UserSubscription> getAll(int page, int size, String sort, String direction) {
        return userSubscriptionDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        userSubscriptionDomainRepository.delete(id);
    }
}
