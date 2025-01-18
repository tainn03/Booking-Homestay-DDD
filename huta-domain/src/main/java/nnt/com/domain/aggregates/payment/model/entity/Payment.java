package nnt.com.domain.aggregates.payment.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.booking.model.entity.Booking;
import nnt.com.domain.common.model.entity.BaseEntity;

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
    String paymentMethod;

    @OneToOne
    Booking booking;
}
