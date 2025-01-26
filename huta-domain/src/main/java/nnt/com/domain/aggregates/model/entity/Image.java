package nnt.com.domain.aggregates.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.shared.model.entity.BaseEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image extends BaseEntity<Long> {
    String url;
    String type;

    @ManyToOne
    @JoinColumn(name = "homestay_id")
    Homestay homestay;
}
