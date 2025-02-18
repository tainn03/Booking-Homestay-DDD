package nnt.com.domain.aggregates.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import nnt.com.domain.shared.model.entity.BaseEntity;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking extends BaseEntity<Long> {
    LocalDate checkIn;
    LocalDate checkOut;
    String code;
    @Lob
    @Column(columnDefinition = "TEXT")
    String note;
    long totalCost;
    int night;
    int adults;
    int children;
    int infants;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    BookingStatus status = BookingStatus.PENDING;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "booking_room",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    List<Room> rooms;

    @OneToOne
    Payment payment;

    @Version
    Integer version;
}
