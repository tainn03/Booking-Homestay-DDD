package nnt.com.domain.aggregates.model.entity;

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
    String note;
    int totalCost;
    int guests;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    BookingStatus status = BookingStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToMany(fetch = FetchType.LAZY)
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
