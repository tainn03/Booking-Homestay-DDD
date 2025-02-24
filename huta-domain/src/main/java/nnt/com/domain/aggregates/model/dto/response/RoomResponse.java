package nnt.com.domain.aggregates.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.entity.Discount;
import nnt.com.domain.aggregates.model.entity.PriceCalendar;
import nnt.com.domain.aggregates.model.entity.RoomAvailable;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class RoomResponse {
    long id;
    String name;
    int size;
    int dailyPrice;
    int weekendPrice;
    String status;
    int beds;

    long homestayId;
    List<AmenityResponse> amenities;
    List<Discount> discounts;
    List<PriceCalendar> priceCalendars;
    List<RoomAvailable> roomAvailables;
}
