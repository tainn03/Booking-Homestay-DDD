package nnt.com.domain.aggregates.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class BookingResponse {
    long id;
    String checkIn;
    String checkOut;
    String code;
    UserResponse user;
    @Lob
    String note;
    String totalCost;
    int night;
    int adults;
    int children;
    int infants;
    BookingStatus status;
    PaymentResponse payment;
    List<Long> roomIds;

    @JsonFormat(pattern = "HH:mm - dd/MM/yyyy")
    LocalDateTime createdAt;

    String homestayName;
    String roomName;
}
