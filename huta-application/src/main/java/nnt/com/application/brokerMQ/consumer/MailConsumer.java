package nnt.com.application.brokerMQ.consumer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
public class MailConsumer {
    MailService mailService;

    @KafkaListener(topics = "user.mail", groupId = "my-group", concurrency = "5")
    public void consumer(ConsumerRecord<String, EmailRequest> record) {
        mailService.send(record.value());
        log.info("LẮNG NGHE SỰ KIỆN VỚI KEY {}, VALUE {}, PARTITION {}, OFFSET {}", record.key(), record.value(), record.partition(), record.offset());
    }
}
