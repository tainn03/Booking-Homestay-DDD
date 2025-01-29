package nnt.com.domain.aggregates.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LoginRequest {
    @NotBlank(message = "EMAIL_MUST_NOT_BE_BLANK")
    @Email(message = "EMAIL_MUST_BE_VALID")
    String email;

    @NotBlank(message = "PWD_MUST_NOT_BE_BLANK")
    String password;
}
