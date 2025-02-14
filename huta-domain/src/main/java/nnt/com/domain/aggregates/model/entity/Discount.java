package nnt.com.domain.aggregates.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.DiscountType;
import nnt.com.domain.shared.model.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Discount extends BaseEntity<Long> {
    double value;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    DiscountType type = DiscountType.DAILY;
    String description;

    @Builder.Default
    LocalDateTime startDate = LocalDateTime.now();

    @Builder.Default
    LocalDateTime endDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    Room room;

    @Version
    Integer version;
}
