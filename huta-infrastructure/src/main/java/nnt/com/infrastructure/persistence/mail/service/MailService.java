package nnt.com.infrastructure.persistence.mail.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.infrastructure.persistence.mail.utils.DynamicTemplatePersonalization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class MailService {
    @Value("${application.sendgrid.api-key}")
    String sendgridApiKey;
    @Value("${application.huta.email}")
    String hutaEmail;

    public String send() throws IOException {
        Map<String, String> dynamicTemplateData = Map.of("name", "hamdi");
        Mail mail = getMail(hutaEmail, "taib2110141@student.ctu.edu.vn",
                "test email", "d-2cebe65b02e545ebaaf91cd34d92a8a7", dynamicTemplateData);
        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        return response.getBody();
    }

    private static Mail getMail(String fromEmail, String toEmail, String subject, String templateId, Map<String, String> dynamicTemplateData) {
        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);
        Mail mail = new Mail();
        DynamicTemplatePersonalization personalization = new DynamicTemplatePersonalization();
        personalization.addTo(to);
        mail.setFrom(from);
        mail.setSubject(subject);
        // This is the name variable that we created on the template
        dynamicTemplateData.forEach(personalization::addDynamicTemplateData);
        mail.addPersonalization(personalization);
        mail.setTemplateId(templateId);
        return mail;
    }

    public String send(String to, String subject, Map<String, String> dynamicTemplateData) throws IOException {
        String demoTemplateId = "d-2cebe65b02e545ebaaf91cd34d92a8a7";
        Mail mail = getMail(hutaEmail, to,
                subject, demoTemplateId, dynamicTemplateData);
        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        Response response = sg.api(request);
        return response.getBody();
    }
}