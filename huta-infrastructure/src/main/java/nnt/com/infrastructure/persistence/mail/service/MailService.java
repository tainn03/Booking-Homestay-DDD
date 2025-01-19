package nnt.com.infrastructure.persistence.mail.service;

import nnt.com.domain.shared.model.dto.EmailRequest;

public interface MailService {
    void send(EmailRequest request);
}
