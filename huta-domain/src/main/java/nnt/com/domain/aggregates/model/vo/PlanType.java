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
public class PlanType {
    String time;
    String ticketPricing;
    String rating;
    String placeName;
    String placeImageUrl;
    String placeDetails;
    String geoCoordinates;
}
