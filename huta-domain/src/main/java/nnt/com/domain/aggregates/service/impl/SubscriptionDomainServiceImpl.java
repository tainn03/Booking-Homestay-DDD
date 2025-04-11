package nnt.com.domain.aggregates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.dto.request.SubscriptionRequest;
import nnt.com.domain.aggregates.model.dto.response.AnalysisSubscriptionResponse;
import nnt.com.domain.aggregates.model.dto.response.SubscriptionsResponse;
import nnt.com.domain.aggregates.model.entity.Role;
import nnt.com.domain.aggregates.model.entity.Subscription;
import nnt.com.domain.aggregates.model.entity.User;
import nnt.com.domain.aggregates.model.entity.UserSubscription;
import nnt.com.domain.aggregates.model.mapper.SubscriptionMapper;
import nnt.com.domain.aggregates.model.vo.RoleType;
import nnt.com.domain.aggregates.repository.RoleDomainRepository;
import nnt.com.domain.aggregates.repository.SubscriptionDomainRepository;
import nnt.com.domain.aggregates.repository.UserDomainRepository;
import nnt.com.domain.aggregates.repository.UserSubscriptionDomainRepository;
import nnt.com.domain.aggregates.service.SubscriptionDomainService;
import nnt.com.domain.shared.exception.BusinessException;
import nnt.com.domain.shared.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SubscriptionDomainServiceImpl implements SubscriptionDomainService {
    SubscriptionDomainRepository subscriptionDomainRepository;
    UserSubscriptionDomainRepository userSubscriptionDomainRepository;
    UserDomainRepository userDomainRepository;
    RoleDomainRepository roleDomainRepository;
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
        updateRoleUser(user);

        // The first time user subscribes, expiredAt must be set to 2 months free trial
        if (user.getUserSubscriptions().isEmpty()) {
            return userSubscriptionDomainRepository.save(UserSubscription.builder()
                    .user(user)
                    .subscription(subscriptionDomainRepository.getById(subscriptionId))
                    .status("INACTIVE")
                    .subscribedAt(LocalDate.now())
                    .expiredAt(LocalDate.now().plusMonths(2))
                    .build());
        }

        UserSubscription currentUserSubscription = user.getUserSubscriptions().stream()
                .max((o1, o2) -> (int) (o1.getExpiredAt().toEpochDay() - o2.getExpiredAt().toEpochDay()))
                .orElse(null);
        if (!isSubscriptionExpired(currentUserSubscription)) {
            if (currentUserSubscription.getSubscription().getId().equals(subscriptionId)) {
                return currentUserSubscription;
            } else {
                throw new BusinessException(ErrorCode.USER_ALREADY_SUBSCRIBED_ANOTHER_PACKAGE);
            }
        } else {
            return userSubscriptionDomainRepository.save(UserSubscription.builder()
                    .user(user)
                    .subscription(subscriptionDomainRepository.getById(subscriptionId))
                    .status("INACTIVE")
                    .subscribedAt(LocalDate.now())
                    .expiredAt(LocalDate.now())
                    .build());
        }
    }

    private void updateRoleUser(User user) {
        if (user.getRole().getRole().equals(RoleType.USER.name())) {
            Role landlord = roleDomainRepository.getById(RoleType.LANDLORD.name());
            user.setRole(landlord);
            userDomainRepository.save(user);
        }
    }

    private boolean isSubscriptionExpired(UserSubscription userSubscription) {
        return userSubscription.getExpiredAt().isBefore(LocalDate.now());
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

    @Override
    public List<AnalysisSubscriptionResponse> getAnalysisSubscriptions(int year) {
        List<Subscription> subscriptions = getSubscriptions();
        List<AnalysisSubscriptionResponse> list = new ArrayList<>();
        subscriptions.forEach(subscription -> {
            long[] monthlyUsers = IntStream.rangeClosed(1, 12)
                    .mapToLong(month -> subscription.getUserSubscriptions().stream()
                            .filter(us -> us.getSubscribedAt().getYear() == year && us.getSubscribedAt().getMonthValue() <= month && us.getExpiredAt().getMonthValue() >= month)
                            .count())
                    .toArray();
            AnalysisSubscriptionResponse response = AnalysisSubscriptionResponse.builder()
                    .name(subscription.getName())
                    .monthlyUsers(monthlyUsers)
                    .amount(subscription.getUserSubscriptions().size())
                    .build();
            list.add(response);
        });
        return list;
    }
}
