package nnt.com.infrastructure.persistence.mail.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import nnt.com.domain.common.model.dto.EmailRequest;
import nnt.com.domain.common.model.enums.MailTemplate;
import nnt.com.infrastructure.persistence.mail.service.MailService;
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
public class MailServiceImpl implements MailService {
    @Value("${application.sendgrid.api-key}")
    String sendgridApiKey;
    @Value("${application.huta.email}")
    String hutaEmail;

    @Override
    public void send(EmailRequest emailRequest) {
        try {
            String demoTemplateId = emailRequest.getTemplateId() != null ? emailRequest.getTemplateId() : MailTemplate.DEMO_TEMPLATE.getValue();
            Mail mail = getMail(hutaEmail, emailRequest.getTo(),
                    emailRequest.getSubject(), demoTemplateId, emailRequest.getTemplateParams());
            SendGrid sg = new SendGrid(sendgridApiKey);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            response.getBody();
        } catch (IOException ex) {
            log.error("ERROR WHEN SENDING EMAIL: {}", ex.getMessage());
        }
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
}