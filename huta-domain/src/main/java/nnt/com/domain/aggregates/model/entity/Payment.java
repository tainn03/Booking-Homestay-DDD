package nnt.com.domain.aggregates.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.PaymentMethod;
import nnt.com.domain.shared.model.entity.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment extends BaseEntity<Long> {
    int amount;
    String transactionId;
    String status;
    String note;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod = PaymentMethod.CASH;

    @OneToOne(mappedBy = "payment")
    @JoinColumn(name = "booking_id", nullable = false)
    Booking booking;

    @OneToOne
    Refund refund;
}
