package nnt.com.domain.aggregates.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.DiscountType;
import nnt.com.domain.shared.model.entity.BaseEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Discount extends BaseEntity<Long> {
    int value;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    DiscountType type = DiscountType.DAILY;

    String status;

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate = LocalDate.now();

    @Builder.Default
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate = LocalDate.now();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    Room room;

    @Version
    Integer version;
}
