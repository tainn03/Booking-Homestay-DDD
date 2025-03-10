package nnt.com.domain.aggregates.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.shared.model.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription extends BaseEntity<Long> {
    String name;
    long price;
    boolean isPopular;
    @ElementCollection
    List<String> features;

    @JsonIgnore
    @OneToMany(mappedBy = "subscription", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    List<UserSubscription> userSubscriptions;
}