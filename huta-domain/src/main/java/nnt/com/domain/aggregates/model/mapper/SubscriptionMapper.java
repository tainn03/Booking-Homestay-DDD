package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.dto.request.SubscriptionRequest;
import nnt.com.domain.aggregates.model.entity.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    Subscription toEntity(SubscriptionRequest request);

    Subscription updateEntity(SubscriptionRequest request, @MappingTarget Subscription subscription);
}
