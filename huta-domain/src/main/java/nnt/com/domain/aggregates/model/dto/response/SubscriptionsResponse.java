package nnt.com.domain.aggregates.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SubscriptionsResponse {
    Long id;
    String status;
    String name;
    long price;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate;
}
