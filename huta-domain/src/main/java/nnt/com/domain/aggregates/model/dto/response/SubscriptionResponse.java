package nnt.com.domain.aggregates.model.dto.response;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SubscriptionResponse {
    String name;
    long price;
    boolean isPopular;
    @Lob
    List<String> features;
    long amount;
    long[] monthlyUsers;
}
