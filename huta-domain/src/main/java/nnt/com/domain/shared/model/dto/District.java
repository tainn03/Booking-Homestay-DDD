package nnt.com.domain.shared.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class District {
    int DistrictID;
    String DistrictName;
    int ProvinceID;
}
