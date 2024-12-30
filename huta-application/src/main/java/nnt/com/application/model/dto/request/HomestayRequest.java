package nnt.com.application.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import nnt.com.application.model.annotation.EnumPattern;
import nnt.com.domain.authentication.model.enums.UserStatus;
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

    @EnumPattern(name = "status", regexp = "ACTIVE|INACTIVE|NONE")
    UserStatus status;

    @GeoPointField
    GeoPoint location;
}
