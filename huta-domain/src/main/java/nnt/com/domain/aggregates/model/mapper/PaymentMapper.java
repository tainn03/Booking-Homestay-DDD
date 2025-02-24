package nnt.com.domain.aggregates.model.mapper;

import nnt.com.domain.aggregates.model.dto.response.PaymentResponse;
import nnt.com.domain.aggregates.model.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "refundId", source = "refund.id")
    PaymentResponse toDTO(Payment payment);
}
