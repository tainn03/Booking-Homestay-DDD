package nnt.com.domain.aggregates.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nnt.com.domain.aggregates.model.entity.Rule;
import nnt.com.domain.aggregates.model.entity.Tag;
import nnt.com.domain.aggregates.model.enums.RentalType;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
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
    Double lon;
    Double lat;
    String addressDetail;
    String emailOwner;

    //    Integer numLike;
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

    List<String> images;

    List<Rule> rules;
    List<Tag> tags;
}
