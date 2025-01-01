package nnt.com.controller.http;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import nnt.com.domain.mail.service.DemoMailService;
import nnt.com.infrastructure.persistence.mail.service.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MailController {
    MailService mailService;
    DemoMailService demoMailService;

    @GetMapping("/send-text")
    public String send() throws IOException {
        return mailService.send();
    }

    @GetMapping("/send-html")
    public String sendHtml() throws IOException {
        Map<String, String> dynamicTemplateData = Map.of("name", "NNT");
        return demoMailService.sendMail("taib2110141@student.ctu.edu.vn", "test email", dynamicTemplateData);
    }
}
