package nnt.com.domain.homestay.model.document;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "homestays")
@Getter
@Setter
public class HomestaySearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String email;
    private String standardCheckIn;
    private String standardCheckOut;
    private String phone;
    private String status;
    private String description;
    private String addressDetail;
    @GeoPointField
    private GeoPoint location;
}
