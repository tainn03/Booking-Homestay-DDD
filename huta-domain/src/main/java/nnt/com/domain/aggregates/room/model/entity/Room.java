package nnt.com.domain.aggregates.room.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.booking.model.entity.Booking;
import nnt.com.domain.common.model.entity.BaseEntity;
import nnt.com.domain.aggregates.homestay.model.entity.Homestay;
import nnt.com.domain.aggregates.image.model.entity.Image;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Room extends BaseEntity<Long> {
    String name;
    int size;
    double price = 0.0;
    double weekendPrice = 0.0;
    String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homestay_id", nullable = false)
    Homestay homestay;

    @ManyToMany(mappedBy = "rooms", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    List<Booking> bookings;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Image> images;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "room_amenity",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    List<Amenity> amenities;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Discount> discounts;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<PriceCalendar> priceCalendars;

    @Version
    Integer version;
}
