package nnt.com.domain.mail.service;

import java.io.IOException;
import java.util.Map;

public interface DemoMailService {
    String sendMail(String to, String subject, Map<String, String> dynamicTemplateData) throws IOException;
}
