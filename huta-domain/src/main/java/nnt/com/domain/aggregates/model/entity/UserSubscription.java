package nnt.com.domain.aggregates.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.shared.model.entity.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSubscription extends BaseEntity<Long> {
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_id", nullable = false)
    Subscription subscription;

    String status;
    LocalDate subscribedAt;
    LocalDate expiredAt;

    @OneToMany(mappedBy = "userSubscription", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Payment> payments;
}
