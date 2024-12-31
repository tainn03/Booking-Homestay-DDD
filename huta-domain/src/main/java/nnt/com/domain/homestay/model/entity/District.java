package nnt.com.domain.homestay.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;
    String name;
    String detail;

    @JsonIgnore
    @ManyToOne
    City city;

    @JsonIgnore
    @OneToMany(mappedBy = "district")
    List<Homestay> homestays;
}
