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
                .to("taib2110141@student.ctu.edu.vn")
                .subject("Test email")
                .templateParams(templateParams)
                .build();
        kafkaProducer.sendAsync(KafkaTopic.MAIL_TOPIC.getTopic(), "key", emailRequest);
        return "Email sent";
    }
}
