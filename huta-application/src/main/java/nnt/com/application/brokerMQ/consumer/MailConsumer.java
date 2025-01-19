package nnt.com.application.brokerMQ.consumer;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.shared.model.dto.EmailRequest;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MailConsumer {

    @KafkaListener(topics = "user.mail", groupId = "my-group")
    public void consumer(ConsumerRecord<String, EmailRequest> record) throws InterruptedException {
        Thread.sleep(5000); // Giả lập xử lý message mất thời gian 5s
        log.info("LẮNG NGHE SỰ KIỆN VỚI KEY {}, VALUE {}, PARTITION {}, OFFSET {}", record.key(), record.value(), record.partition(), record.offset());
    }
}
