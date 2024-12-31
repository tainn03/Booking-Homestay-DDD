package nnt.com.domain.homestay.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class TypeHomestay {
    @Id
    String name;
    String urlImage;

    @OneToMany(mappedBy = "typeHomestay")
    @JsonIgnore
    List<Homestay> homestays;
}
