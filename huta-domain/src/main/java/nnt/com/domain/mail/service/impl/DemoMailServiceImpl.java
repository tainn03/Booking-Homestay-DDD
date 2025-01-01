package nnt.com.domain.mail.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.mail.repository.DemoMailRepository;
import nnt.com.domain.mail.service.DemoMailService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DemoMailServiceImpl implements DemoMailService {
    DemoMailRepository demoMailRepository;

    @Override
    public String sendMail(String to, String subject, Map<String, String> dynamicTemplateData) throws IOException {
        return demoMailRepository.sendMail(to, subject, dynamicTemplateData);
    }
}
