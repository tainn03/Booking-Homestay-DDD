package nnt.com.controller.http;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.base.model.dto.EmailRequest;
import nnt.com.domain.base.model.enums.KafkaTopic;
import nnt.com.infrastructure.distributed.kafka.producer.KafkaProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MailController {
    KafkaProducer kafkaProducer;

    @GetMapping
    public String sendHtml() {
        Map<String, String> templateParams = Map.of("name", "NNT");
        EmailRequest emailRequest = EmailRequest.builder()
                .templateParams(templateParams)
                .build();
        String[] keys = {"first_key", "second_key", "third_key"};
        for (int i = 0; i < 3; i++) {
            emailRequest.setTo("test " + i);
            emailRequest.setSubject(String.valueOf(i));
            kafkaProducer.sendAsync(KafkaTopic.MAIL_TOPIC.getTopic(), keys[i], emailRequest);
        }
        return "Email sent";
    }
}
