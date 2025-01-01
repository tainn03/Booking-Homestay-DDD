package nnt.com.domain.mail.repository;

import java.io.IOException;
import java.util.Map;

public interface DemoMailRepository {
    String sendMail(String to, String subject, Map<String, String> dynamicTemplateData) throws IOException;
}
