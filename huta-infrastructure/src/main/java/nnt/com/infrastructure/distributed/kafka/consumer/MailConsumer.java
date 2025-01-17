package nnt.com.infrastructure.distributed.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.base.model.dto.EmailRequest;
import nnt.com.infrastructure.persistence.mail.service.MailService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MailConsumer {
    MailService mailService;

    //    @KafkaListener(topics = "#{T(nnt.com.domain.base.model.enums.KafkaTopic).MAIL_TOPIC.getTopic()}", groupId = "my-group")
    @KafkaListener(topics = "user.mail", groupId = "my-group")
    public void consume(ConsumerRecord<String, EmailRequest> record) throws IOException {
        log.info("LẮNG NGHE MAIL VỚI KEY {}, VALUE {}, PARTITION {}, OFFSET {}", record.key(), record.value(), record.partition(), record.offset());
//        mailService.send(record.value());
    }
}
