package nnt.com.domain.payment.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.model.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Refund extends BaseEntity<Long> {
    LocalDateTime date;
    String reason;
    String status;
    int amount;

    @OneToOne
    Payment payment;
}
