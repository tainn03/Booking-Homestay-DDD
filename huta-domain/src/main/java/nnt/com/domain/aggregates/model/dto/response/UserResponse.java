package nnt.com.domain.aggregates.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    long id;
    String email;
    String fullName;
    String avatar;
    UserStatus status;
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
    LocalDateTime lastLogin;
    String role;
}
