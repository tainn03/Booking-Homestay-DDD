package nnt.com.domain.aggregates.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.HomestayStatus;
import nnt.com.domain.aggregates.model.vo.RentalType;
import nnt.com.domain.shared.model.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "homestays")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Homestay extends BaseEntity<Long> implements Serializable {
    String homestayName;
    String email;
    String standardCheckIn;
    String standardCheckOut;
    String phone;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    HomestayStatus status = HomestayStatus.ACTIVE;

    String description;
    double lon;
    double lat;
    String addressDetail;
    int bathrooms;
    int bedrooms;
    int kitchens;
    int beds;
    int maxGuests;
    int maxNights;
    int minNights;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    RentalType rentalType = RentalType.ALL;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User owner;

    @ManyToMany(mappedBy = "wishlist", fetch = FetchType.LAZY)
    List<User> likedUsers;

    @OneToMany(mappedBy = "homestay", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Image> images;

    @OneToMany(mappedBy = "homestay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Room> rooms;

    @OneToMany(mappedBy = "homestay", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "name")
    TypeHomestay typeHomestay;

    @ManyToOne
    @JoinColumn(name = "district_id")
    District district;

    @OneToMany(mappedBy = "homestay", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Rule> rules;

    @OneToMany(mappedBy = "homestay", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Tag> tags;

    @OneToMany(mappedBy = "homestay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Recommend> recommends;

    @Version
    Integer version;
}
