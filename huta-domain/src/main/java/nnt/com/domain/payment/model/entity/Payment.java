package nnt.com.domain.payment.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.model.entity.BaseEntity;
import nnt.com.domain.booking.model.entity.Booking;

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
