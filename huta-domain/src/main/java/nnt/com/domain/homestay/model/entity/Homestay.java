package nnt.com.domain.homestay.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.model.entity.BaseEntity;

import java.io.Serializable;

@Entity
@Table(name = "homestays")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Homestay extends BaseEntity<Long> implements Serializable {
    String name;
    String email;
    String standardCheckIn;
    String standardCheckOut;
    String phone;
    String status;
    String description;
    long lon;
    long lat;
    String addressDetail;

    @Version
    @Builder.Default
    Long version = 0L;
}
