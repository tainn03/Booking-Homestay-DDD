package nnt.com.domain.aggregates.model.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.HomestayStatus;
import nnt.com.domain.aggregates.model.vo.RentalType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class HomestayRequest {
    @NotBlank(message = "NAME_REQUIRED")
    String name;
    String email;
    String standardCheckIn;
    String standardCheckOut;
    String phone;
    String description;
    String emailOwner;
    String typeHomestay;
    int bathrooms;
    int bedrooms;
    int kitchens;
    int beds;
    int maxGuests;
    int maxNights;
    int minNights;
    int dailyPrice;
    int weekendPrice;
    float monthlyDiscount;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    HomestayStatus status = HomestayStatus.ACTIVE;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    RentalType rentalType = RentalType.ENTIRE_PLACE;

    String ward;
    String district;
    String city;
    String addressDetail;
    @GeoPointField
    GeoPoint location;

    List<RuleRequest> rules;
    List<String> tags;
    int acreage;
    List<String> amenities;
    List<CustomAmenityRequest> customAmenities;
    int refundValue;
    List<TypeImageRequest> typeImage;
}
