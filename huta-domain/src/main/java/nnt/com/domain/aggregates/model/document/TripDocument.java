package nnt.com.domain.aggregates.model.document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.HotelOptionType;
import nnt.com.domain.aggregates.model.vo.ItineraryType;
import nnt.com.domain.aggregates.model.vo.UserSelection;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Document(indexName = "trip_plans")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class TripDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;

    String userEmail;

    List<HotelOptionType> hotelOptions;
    List<ItineraryType> itinerary;
    UserSelection userSelection;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @Builder.Default
    Instant createdAt = Instant.now();
}
