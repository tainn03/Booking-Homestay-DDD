package nnt.com.application.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
}
