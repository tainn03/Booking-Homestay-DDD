package nnt.com.domain.aggregates.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PriceResponse {
    String originalCost;
    String discountValue;
    String totalCost;
    int dailyDays;
    String dailyPrice;
    int weekendDays;
    String weekendPrice;
}
