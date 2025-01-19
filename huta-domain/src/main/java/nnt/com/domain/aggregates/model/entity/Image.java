package nnt.com.domain.aggregates.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.enums.ImageType;
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

    @Enumerated(EnumType.STRING)
    ImageType type;

    @ManyToOne
    @JoinColumn(name = "homestay_id")
    Homestay homestay;
}
