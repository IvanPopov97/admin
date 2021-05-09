package ru.admin.service;

import freemarker.template.TemplateException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private final FreeMarkerConfigurer freemarkerConfigurer;

    public EmailService(JavaMailSender emailSender, FreeMarkerConfigurer freemarkerConfigurer) {
        this.emailSender = emailSender;
        this.freemarkerConfigurer = freemarkerConfigurer;
    }

    private void sendHtml(String to, String subject, String html) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        emailSender.send(message);
    }

    public void sendTemplate(String to, String subject, String templateName, Map<String, Object> templateModel) {
        try {
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(
                    freemarkerConfigurer.getConfiguration().getTemplate(templateName),
                    templateModel
            );
            sendHtml(to, subject, html);
        }
        catch (IOException | TemplateException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
