package nnt.com.domain.aggregates.homestay.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.common.model.entity.BaseEntity;
import nnt.com.domain.aggregates.image.model.entity.Image;
import nnt.com.domain.aggregates.room.model.entity.Room;
import nnt.com.domain.aggregates.user.model.entity.User;

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
    String status;
    String description;
    long lon;
    long lat;
    String addressDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User owner;

    @ManyToMany(mappedBy = "wishlist", fetch = FetchType.LAZY)
    List<User> likedUsers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Image> images;

    @OneToMany(mappedBy = "homestay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Room> rooms;

    @OneToMany(mappedBy = "homestay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Rating> reviews;

    @ManyToOne
    @JoinColumn(name = "name", nullable = false)
    TypeHomestay typeHomestay;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false)
    District district;

    @Version
    Integer version;
}
