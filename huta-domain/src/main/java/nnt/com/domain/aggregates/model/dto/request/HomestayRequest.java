package nnt.com.domain.aggregates.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import nnt.com.domain.aggregates.model.enums.UserStatus;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomestayRequest {
    Long id;
    @NotBlank(message = "NAME_REQUIRED")
    String name;
    String email;
    String standardCheckIn;
    String standardCheckOut;
    String phone;
    String description;
    String addressDetail;

    UserStatus status;

    @GeoPointField
    GeoPoint location;

    String emailOwner;
    String typeHomestay;
    String district;
    String city;
}
