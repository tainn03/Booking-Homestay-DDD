package nnt.com.application.brokerMQ.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.application.brokerMQ.producer.MailProducer;
import nnt.com.domain.shared.model.dto.EmailRequest;
import nnt.com.domain.shared.model.vo.KafkaTopic;
import nnt.com.domain.shared.model.vo.MailTemplate;
import nnt.com.infrastructure.distributed.kafka.producer.KafkaProducer;
import org.springframework.stereotype.Service;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class MailProducerImpl implements MailProducer {
    KafkaProducer kafkaProducer;

    @Override
    public void sendMail(String key, EmailRequest emailRequest) {
        kafkaProducer.sendAsync(KafkaTopic.MAIL_TOPIC.getTopic(), key, emailRequest);
    }

    @Override
    public void sendForgotPasswordMail(String email, String password) {
        EmailRequest emailRequest = EmailRequest.builder()
                .to(email)
                .subject("Quên mật khẩu")
                .templateId(MailTemplate.FORGOT_PASSWORD.getValue())
                .templateParams(Map.of("pwd", password))
                .build();
        sendMail(email, emailRequest);
    }

    @Override
    public void sendRegisterMail(String email, String name) {
        EmailRequest emailRequest = EmailRequest.builder()
                .to(email)
                .subject("Đăng ký thành công")
                .templateId(MailTemplate.REGISTER_LANDLORD.getValue())
                .templateParams(Map.of("name", name))
                .build();
        sendMail(email, emailRequest);
    }

    @Override
    public void sendBookingMail(String email, String name) {
        log.info("SEND BOOKING MAIL TO {}", email);
//        EmailRequest emailRequest = EmailRequest.builder()
//                .to(email)
//                .subject("Xác nhận đặt phòng")
//                .templateId(MailTemplate.BOOKING.getValue())
//                .templateParams(Map.of("name", name, "code", code))
//                .build();
//        sendMail(email, emailRequest);
    }

}
