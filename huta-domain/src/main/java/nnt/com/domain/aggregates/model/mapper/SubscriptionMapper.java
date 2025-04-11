package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.dto.request.SubscriptionRequest;
import nnt.com.domain.aggregates.model.dto.response.SubscriptionResponse;
import nnt.com.domain.aggregates.model.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    Subscription toEntity(SubscriptionRequest request);

    @Mapping(target = "amount", expression = "java(subscription.getUserSubscriptions().size())")
    @Mapping(target = "monthlyUsers", ignore = true)
    SubscriptionResponse toDTO(Subscription subscription);

    Subscription updateEntity(SubscriptionRequest request, @MappingTarget Subscription subscription);
}
