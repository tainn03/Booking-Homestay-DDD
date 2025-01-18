package nnt.com.domain.aggregates.user.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.booking.model.entity.Booking;
import nnt.com.domain.aggregates.user.model.enums.UserStatus;
import nnt.com.domain.common.model.entity.BaseEntity;
import nnt.com.domain.aggregates.homestay.model.entity.Homestay;
import nnt.com.domain.aggregates.homestay.model.entity.Rating;
import nnt.com.domain.aggregates.image.model.entity.Image;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email", unique = true)
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity<Long> implements UserDetails {
    String email;
    String password;
    String fullName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    UserStatus status = UserStatus.ACTIVE;
    String phone;
    LocalDate dob;
    String gender;
    String address;
    String cccd;
    String businessLicense;
    String nationality;
    String bankName;
    String bankNum;
    String bankUsername;
    LocalDateTime lastLogin;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    Image avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role", nullable = false)
    Role role;

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    List<Homestay> homestays;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "users_wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "homestay_id")
    )
    List<Homestay> wishlist;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    List<Booking> bookings;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    List<Rating> reviews;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
