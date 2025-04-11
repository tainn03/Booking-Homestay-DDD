package nnt.com.domain.aggregates.service;

import nnt.com.domain.aggregates.model.dto.request.SubscriptionRequest;
import nnt.com.domain.aggregates.model.dto.response.AnalysisSubscriptionResponse;
import nnt.com.domain.aggregates.model.dto.response.SubscriptionsResponse;
import nnt.com.domain.aggregates.model.entity.Subscription;
import nnt.com.domain.aggregates.model.entity.UserSubscription;
import nnt.com.domain.shared.behaviors.BaseBehaviors;

import java.util.List;

public interface SubscriptionDomainService extends BaseBehaviors<Subscription, Long> {
    Subscription createSubscription(SubscriptionRequest request);

    List<Subscription> getSubscriptions();

    Subscription updateSubscription(Long subscriptionId, SubscriptionRequest request);

    void deleteSubscription(Long subscriptionId);

    UserSubscription subscribe(Long subscriptionId);

    List<SubscriptionsResponse> getMySubscriptions();

    List<AnalysisSubscriptionResponse> getAnalysisSubscriptions(int year);
}
