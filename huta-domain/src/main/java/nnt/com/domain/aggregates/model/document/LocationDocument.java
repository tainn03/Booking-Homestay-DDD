package nnt.com.domain.aggregates.model.document;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.Document;

import static lombok.AccessLevel.PRIVATE;

@Document(indexName = "locations")
@Data
@Builder
@FieldDefaults(level = PRIVATE)
public class LocationDocument {
    @Id
    String id;
    String ward;
    String district;
    String city;
}
