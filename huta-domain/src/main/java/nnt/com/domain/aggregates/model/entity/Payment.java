package nnt.com.domain.aggregates.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    long amount;
    String transactionId;
    String status;
    String note;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod = PaymentMethod.CASH;

    @JsonIgnore
    @OneToOne(mappedBy = "payment")
    @JoinColumn(name = "booking_id")
    Booking booking;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_subscription_id")
    UserSubscription userSubscription;

    @OneToOne
    Refund refund;
}
