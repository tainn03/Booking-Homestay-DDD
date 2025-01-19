package nnt.com.application.brokerMQ.producer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.shared.model.dto.EmailRequest;
import nnt.com.domain.shared.model.enums.KafkaTopic;
import nnt.com.infrastructure.distributed.kafka.producer.KafkaProducer;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MailProducer {
    KafkaProducer kafkaProducer;

    public void sendMail(String key, EmailRequest emailRequest) {
        kafkaProducer.sendAsync(KafkaTopic.MAIL_TOPIC.getTopic(), key, emailRequest);
    }
}
