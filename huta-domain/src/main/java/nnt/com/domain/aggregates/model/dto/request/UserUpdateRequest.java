package nnt.com.domain.aggregates.model.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String fullName;
    String phone;
    LocalDate dob;
    String gender;
    String address;
    String identityNumber;
    String businessLicense;
    String nationality;
    String bankName;
    String bankNum;
    String bankUsername;
}
