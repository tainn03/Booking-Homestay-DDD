package nnt.com.domain.aggregates.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.PaymentMethod;
import nnt.com.domain.shared.model.entity.BaseEntity;

import java.time.LocalDate;

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
    LocalDate date;
    String status;
    String note;

    @Enumerated(EnumType.STRING)
    PaymentMethod paymentMethod;

    @OneToOne(mappedBy = "payment")
    @JoinColumn(name = "booking_id", nullable = false)
    Booking booking;

    @OneToOne
    Refund refund;
}
