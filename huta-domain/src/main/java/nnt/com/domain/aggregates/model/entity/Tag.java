package nnt.com.domain.aggregates.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.shared.model.entity.BaseEntity;

import static lombok.AccessLevel.PRIVATE;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = PRIVATE)
public class Tag extends BaseEntity<Long> {
    String tag;

    @ManyToOne
    @JoinColumn(name = "homestay_id")
    Homestay homestay;
}
