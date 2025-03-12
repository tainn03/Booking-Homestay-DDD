package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.dto.request.SubscriptionRequest;
import nnt.com.domain.aggregates.model.dto.response.SubscriptionsResponse;
import nnt.com.domain.aggregates.model.entity.Subscription;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.model.entity.UserSubscription;
import nnt.com.domain.aggregates.model.mapper.SubscriptionMapper;
import nnt.com.domain.aggregates.repository.SubscriptionDomainRepository;
import nnt.com.domain.aggregates.repository.UserDomainRepository;
import nnt.com.domain.aggregates.repository.UserSubscriptionDomainRepository;
import nnt.com.domain.aggregates.service.SubscriptionDomainService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SubscriptionDomainServiceImpl implements SubscriptionDomainService {
    SubscriptionDomainRepository subscriptionDomainRepository;
    UserSubscriptionDomainRepository userSubscriptionDomainRepository;
    UserDomainRepository userDomainRepository;
    SubscriptionMapper subscriptionMapper;

    @Override
    public Subscription save(Subscription subscription) {
        return subscriptionDomainRepository.save(subscription);
    }

    @Override
    public Subscription update(Subscription subscription) {
        return subscriptionDomainRepository.update(subscription);
    }

    @Override
    public Subscription getById(Long id) {
        return subscriptionDomainRepository.getById(id);
    }

    @Override
    public Page<Subscription> getAll(int page, int size, String sort, String direction) {
        return subscriptionDomainRepository.getAll(page, size, sort, direction);
    }

    @Override
    public void delete(Long id) {
        subscriptionDomainRepository.delete(id);
    }

    @Override
    public Subscription createSubscription(SubscriptionRequest request) {
        Subscription subscription = subscriptionMapper.toEntity(request);
        return subscriptionDomainRepository.save(subscription);
    }

    @Override
    public List<Subscription> getSubscriptions() {
        return subscriptionDomainRepository.getAll();
    }

    @Override
    public Subscription updateSubscription(Long subscriptionId, SubscriptionRequest request) {
        Subscription subscription = subscriptionDomainRepository.getById(subscriptionId);
        subscription = subscriptionMapper.updateEntity(request, subscription);
        return subscriptionDomainRepository.update(subscription);
    }

    @Override
    public void deleteSubscription(Long subscriptionId) {
        subscriptionDomainRepository.delete(subscriptionId);
    }

    @Override
    public UserSubscription subscribe(Long subscriptionId) {
        User user = userDomainRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getUserSubscriptions() != null) {
            for (UserSubscription userSubscription : user.getUserSubscriptions()) {
                if (userSubscription.getSubscription().getId().equals(subscriptionId)) {
                    return userSubscription;
                }
            }
        }
        UserSubscription userSubscription = UserSubscription.builder()
                .user(user)
                .subscription(subscriptionDomainRepository.getById(subscriptionId))
                .status("INACTIVE")
                .subscribedAt(LocalDate.now())
                .expiredAt(LocalDate.now())
                .build();
        return userSubscriptionDomainRepository.save(userSubscription);
    }

    @Override
    public List<SubscriptionsResponse> getMySubscriptions() {
        User user = userDomainRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<UserSubscription> subscriptions = user.getUserSubscriptions();
        return subscriptions.stream()
                .map(subscription -> SubscriptionsResponse.builder()
                        .id(subscription.getId())
                        .name(subscription.getSubscription().getName())
                        .price(subscription.getSubscription().getPrice())
                        .status(subscription.getStatus())
                        .startDate(subscription.getSubscribedAt())
                        .endDate(subscription.getExpiredAt())
                        .build())
                .collect(Collectors.toList());
    }
}
