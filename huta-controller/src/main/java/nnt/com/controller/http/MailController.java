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
//        Map<String, String> templateParams = Map.of("name", "NNT");
//        EmailRequest emailRequest = EmailRequest.builder()
//                .to("taib2110141@student.ctu.edu.vn")
//                .subject("Test email")
//                .templateParams(templateParams)
//                .build();
//        kafkaProducer.sendAsync(KafkaTopic.MAIL_TOPIC.getTopic(), null, emailRequest);
        Map<String, String> templateParams = Map.of("name", "NNT");
        EmailRequest emailRequest = EmailRequest.builder()
                .to("test 1")
                .subject("1")
                .templateParams(templateParams)
                .build();
        kafkaProducer.sendFireAndForgot(KafkaTopic.MAIL_TOPIC.getTopic(), "1", emailRequest);
        emailRequest = EmailRequest.builder()
                .to("test 2")
                .subject("2")
                .templateParams(templateParams)
                .build();
        kafkaProducer.sendFireAndForgot(KafkaTopic.MAIL_TOPIC.getTopic(), "2", emailRequest);
        emailRequest = EmailRequest.builder()
                .to("test 3")
                .subject("3")
                .templateParams(templateParams)
                .build();
        kafkaProducer.sendFireAndForgot(KafkaTopic.MAIL_TOPIC.getTopic(), "3", emailRequest);
        return "Email sent";
    }
}
