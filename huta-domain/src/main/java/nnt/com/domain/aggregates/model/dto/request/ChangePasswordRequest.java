package nnt.com.domain.aggregates.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    @NotBlank(message = "EMAIL_MUST_NOT_BE_BLANK")
    @Email(message = "EMAIL_MUST_BE_VALID")
    String email;

    @NotBlank(message = "PWD_MUST_NOT_BE_BLANK")
    String currentPassword;

    @NotBlank(message = "PWD_MUST_NOT_BE_BLANK")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[a-zA-Z\\d@$!%*?&]{8,}$", message = "PASSWORD_MUST_BE_STRONG")
    String newPassword;
}
