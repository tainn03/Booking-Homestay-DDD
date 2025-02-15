package nnt.com.domain.aggregates.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nnt.com.domain.aggregates.model.dto.request.RuleRequest;
import nnt.com.domain.aggregates.model.vo.RentalType;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomestayResponse {
    long id;
    String date;
    String title;
    String email;
    String standardCheckIn;
    String standardCheckOut;
    String phone;
    String status;
    String description;
    double lon;
    double lat;
    String address;
    String authorId;

    String featuredImage;
    List<String> galleryImgs;
    int commentCount;
    int viewCount;
    boolean like;
    float reviewStart;
    int reviewCount;
    String price;
    String saleOff;
    boolean isAds;
    Map<String, Double> map;
    //    List<Room> rooms;
    double rating;
    String typeHomestay;
    Integer version;

    int bathrooms;
    int bedrooms;
    int kitchens;
    int beds;
    int maxGuests;
    int maxNights;
    int minNights;
    int acreage;
    int refundValue;

    RentalType rentalType;

    List<RuleRequest> rules;
    List<String> tags;
}
