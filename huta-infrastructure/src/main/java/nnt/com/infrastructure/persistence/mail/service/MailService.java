package nnt.com.infrastructure.persistence.mail.service;

import nnt.com.domain.base.model.dto.EmailRequest;

public interface MailService {
    void send(EmailRequest request);
}
