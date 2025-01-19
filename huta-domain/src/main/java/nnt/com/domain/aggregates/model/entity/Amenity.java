package nnt.com.domain.aggregates.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import nnt.com.domain.aggregates.model.enums.AmenityType;

import java.util.List;

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
    AmenityType type;

    @JsonIgnore
    @ManyToMany(mappedBy = "amenities", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Room> rooms;
}
