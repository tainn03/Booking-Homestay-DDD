package nnt.com.domain.aggregates.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.shared.model.entity.BaseEntity;

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

    @OneToOne(mappedBy = "refund")
    @JoinColumn(name = "payment_id", nullable = false)
    Payment payment;
}
