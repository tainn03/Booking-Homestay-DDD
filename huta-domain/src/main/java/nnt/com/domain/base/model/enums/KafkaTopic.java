package nnt.com.domain.base.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Getter
public enum KafkaTopic {
    MAIL_TOPIC("user.mail"),
    BOOKING_TOPIC("booking.mail"),
    PAYMENT_TOPIC("payment.mail"),
    RECOMMENDATION_TOPIC("homestay.recommendation");


    String topic;
}
