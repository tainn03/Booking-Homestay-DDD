package nnt.com.domain.aggregates.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import nnt.com.domain.aggregates.model.vo.AmenityType;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Amenity {
    @Id
    String name;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    AmenityType type = AmenityType.DEFAULT;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    Room room;
}
