package nnt.com.domain.aggregates.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

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
    String type;

    @JsonIgnore
    @ManyToMany(mappedBy = "amenities", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Room> rooms;
}
