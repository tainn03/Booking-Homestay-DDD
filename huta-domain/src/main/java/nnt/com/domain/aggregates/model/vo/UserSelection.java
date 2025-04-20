package nnt.com.domain.aggregates.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.shared.model.dto.District;
import nnt.com.domain.shared.model.dto.Province;
import nnt.com.domain.shared.model.dto.Ward;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class UserSelection {
    int selectedDay;
    String selectedTraveler;
    String selectedBudget;
    Province selectedProvince;
    District selectedDistrict;
    Ward selectedWard;
}

