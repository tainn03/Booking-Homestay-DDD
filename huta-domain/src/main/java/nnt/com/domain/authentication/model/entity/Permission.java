package nnt.com.domain.authentication.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.model.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity<Long> {
    String permission;
    String apiPath;
    String module;
    String method;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Role> roles;

}
