package nnt.com.domain.authentication.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.authentication.model.enums.UserStatus;
import nnt.com.domain.base.model.entity.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

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

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
