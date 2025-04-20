package nnt.com.infrastructure.distributed.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.entity.Booking;
import nnt.com.domain.aggregates.model.entity.Payment;
import nnt.com.domain.aggregates.model.vo.BookingStatus;
import nnt.com.domain.aggregates.service.BookingDomainService;
import nnt.com.domain.aggregates.service.PaymentDomainService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PaymentConsumer {
    PaymentDomainService paymentService;
    BookingDomainService bookingDomainService;

    @KafkaListener(topics = "payment.mail", groupId = "my-group", concurrency = "5")
    public void consumer(ConsumerRecord<String, Payment> record) {
        log.info("LẮNG NGHE SỰ KIỆN VỚI KEY {}, VALUE {}, PARTITION {}, OFFSET {}", record.key(), record.value(), record.partition(), record.offset());
        Payment payment = record.value();
        String bookingCode = record.key();
        Booking booking = bookingDomainService.getByCode(bookingCode);
        payment.setBooking(booking);
        payment = paymentService.save(payment);
        booking.setPayment(payment);
        booking.setStatus(BookingStatus.PAID);
        bookingDomainService.update(booking);
        log.info("ĐÃ LƯU THANH TOÁN VÀ CẬP NHẬT TRẠNG THÁI ĐẶT PHÒNG THÀNH CÔNG");
        log.info("GỬI MAIL CHO KHÁCH HÀNG");
        // Gửi mail cho khách hàng
    }
}
