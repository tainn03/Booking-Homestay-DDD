package nnt.com.infrastructure.distributed.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.aggregates.model.dto.request.BookingRequest;
import nnt.com.domain.aggregates.service.BookingDomainService;
import nnt.com.domain.shared.model.dto.EmailRequest;
import nnt.com.infrastructure.persistence.mail.service.MailService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BookingConsumer {
    MailService mailService;
    BookingDomainService bookingDomainService;

    @KafkaListener(topics = "booking.mail", groupId = "my-group")
    public void consumer(ConsumerRecord<String, EmailRequest> record) {
        log.info("LẮNG NGHE SỰ KIỆN VỚI KEY {}, VALUE {}, PARTITION {}, OFFSET {}", record.key(), record.value(), record.partition(), record.offset());
    }

    @KafkaListener(topics = "booking.confirmation", groupId = "my-group", concurrency = "3")
    public void consumerConfirmation(ConsumerRecord<String, BookingRequest> record) {
        log.info("LẮNG NGHE SỰ KIỆN VỚI KEY {}, VALUE {}, PARTITION {}, OFFSET {}", record.key(), record.value(), record.partition(), record.offset());
        String code = record.key();
        BookingRequest request = record.value();
        bookingDomainService.booking(request, code);
        log.info("ĐẶT PHÒNG THÀNH CÔNG");
        log.info("GỬI MAIL CHO KHÁCH HÀNG VỀ VIỆC ĐẶT PHÒNG, EMAIL: {}", request.getEmail());
        // gửi mail cho khách hàng
    }
}
