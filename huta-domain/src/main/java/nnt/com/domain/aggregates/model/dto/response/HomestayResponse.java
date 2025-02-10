package nnt.com.domain.aggregates.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nnt.com.domain.aggregates.model.dto.request.RuleRequest;
import nnt.com.domain.aggregates.model.dto.request.TagRequest;
import nnt.com.domain.aggregates.model.vo.RentalType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomestayResponse {
    long id;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String name;
    String email;
    String standardCheckIn;
    String standardCheckOut;
    String phone;
    String status;
    String description;
    double lon;
    double lat;
    String addressDetail;
    String emailOwner;

    //    List<String> imageUrls;
    //    List<Room> rooms;
//    List<Rating> reviews;
    String typeHomestay;
    Integer version;

    int bathrooms;
    int bedrooms;
    int kitchens;
    int beds;
    int maxGuests;
    int maxNights;
    int minNights;

    RentalType rentalType;

    List<RuleRequest> rules;
    List<TagRequest> tags;
}
