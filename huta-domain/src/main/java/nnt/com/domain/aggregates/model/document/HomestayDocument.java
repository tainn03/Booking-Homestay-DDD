package nnt.com.domain.aggregates.model.document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.dto.request.RuleRequest;
import nnt.com.domain.aggregates.model.vo.RentalType;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Document(indexName = "homestays")
@Getter
@Setter
@FieldDefaults(level = PRIVATE)
public class HomestayDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String date;
    String title;
    String email;
    String standardCheckIn;
    String standardCheckOut;
    String phone;
    String status;
    String description;
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
    @GeoPointField
    private GeoPoint location;
}
