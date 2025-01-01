package nnt.com.infrastructure.persistence.mail.repository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.mail.repository.DemoMailRepository;
import nnt.com.infrastructure.persistence.mail.service.MailService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DemoMailRepositoryImpl implements DemoMailRepository {
    MailService mailService;

    @Override
    public String sendMail(String to, String subject, Map<String, String> dynamicTemplateData) throws IOException {
        return mailService.send(to, subject, dynamicTemplateData);
    }
}
