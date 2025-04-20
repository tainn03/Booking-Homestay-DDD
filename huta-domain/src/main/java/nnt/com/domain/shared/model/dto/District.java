package nnt.com.domain.shared.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class District {
    int DistrictID;
    String DistrictName;
    int ProvinceID;
}
