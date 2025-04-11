package nnt.com.domain.aggregates.model.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.Document;

import static lombok.AccessLevel.PRIVATE;

@Document(indexName = "user_action_logss")
@Getter
@Setter
@Builder
@FieldDefaults(level = PRIVATE)
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
    String action;
    String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    String timestamp;
    boolean success;
}
