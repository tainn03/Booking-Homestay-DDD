package nnt.com.domain.aggregates.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class RoomRequest {
    long homestayId;
    String name;
    int size;
    int dailyPrice;
    int weekendPrice;
    String status;
    int beds;
}
