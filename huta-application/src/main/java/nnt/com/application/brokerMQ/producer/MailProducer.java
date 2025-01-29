package nnt.com.application.brokerMQ.producer;

import nnt.com.domain.shared.model.dto.EmailRequest;

public interface MailProducer {
    void sendMail(String key, EmailRequest emailRequest);

    void sendForgotPasswordMail(String email, String password);
}
