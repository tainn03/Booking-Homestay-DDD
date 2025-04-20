package nnt.com.domain.aggregates.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class HotelOptionType {
    String hotelName;
    String price;
    String rating;
    String hotelAddress;
    String geoCoordinates;
    String description;
}

