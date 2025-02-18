package nnt.com.domain.aggregates.model.dto.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class BookingRequest {
    long homestayId;
    long roomId;
    LocalDate checkIn;
    LocalDate checkOut;
    int adult;
    int children;
    int infant;
    long totalCost;

    @Email(message = "EMAIL_MUST_BE_VALID")
    String email;
    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "PHONE_MUST_BE_VALID")
    String phone;
    @Lob
    String note;
}
