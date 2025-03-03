package nnt.com.domain.aggregates.model.dto.response;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.aggregates.model.vo.BookingStatus;

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
    String email;
    @Lob
    String note;
    String totalCost;
    int night;
    int adults;
    int children;
    int infants;
    BookingStatus status;
    PaymentResponse payment;
    List<String> roomNames;
}
