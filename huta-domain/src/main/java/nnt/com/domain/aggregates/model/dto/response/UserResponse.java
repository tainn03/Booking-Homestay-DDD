package nnt.com.domain.aggregates.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.UserStatus;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @Lob
    String bio;

    @JsonFormat(pattern = "'ngày' dd 'tháng' MM, yyyy")
    LocalDateTime createdAt;
}
