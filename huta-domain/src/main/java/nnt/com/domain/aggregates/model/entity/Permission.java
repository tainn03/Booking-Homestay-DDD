package nnt.com.domain.aggregates.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {
    @Id
    String permission;
    String apiPath;
    String module;
    String method;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Role> roles;

}
