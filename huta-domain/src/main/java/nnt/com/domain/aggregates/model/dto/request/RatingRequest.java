package nnt.com.domain.aggregates.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest {
    Integer rating;
    String comment;
    LocalDateTime createdAt;
}
