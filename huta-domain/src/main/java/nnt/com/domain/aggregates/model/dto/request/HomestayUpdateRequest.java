package nnt.com.domain.aggregates.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.RentalType;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class HomestayUpdateRequest {
    String name;
    String email;
    String standardCheckIn;
    String standardCheckOut;
    String phone;
    String addressDetail;
    int acreage;
    String typeHomestay;
    int bathrooms;
    int kitchens;
    int minNights;
    int maxNights;
    RentalType rentalType;
    String description;
}
