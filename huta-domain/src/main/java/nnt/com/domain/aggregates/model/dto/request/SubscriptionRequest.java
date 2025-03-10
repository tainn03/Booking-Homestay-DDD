package nnt.com.domain.aggregates.model.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SubscriptionRequest {
    String name;
    long price;
    boolean isPopular;
    List<String> features;
}
