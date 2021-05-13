package ru.admin.service;

import freemarker.template.TemplateException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.enitity.ConfirmationToken;
import ru.admin.enitity.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

@Service
public class UserEmailService {

    private final EmailService emailService;
    private final FreeMarkerConfigurer freemarkerConfigurer;
    private final AccountActivationProperties accountActivationProperties;

    public UserEmailService(EmailService emailService, FreeMarkerConfigurer freemarkerConfigurer,
            AccountActivationProperties accountActivationProperties) {
        this.emailService = emailService;
        this.freemarkerConfigurer = freemarkerConfigurer;
        this.accountActivationProperties = accountActivationProperties;
    }

    public void sendAccountActivationEmail(User user, ConfirmationToken confirmationToken)
            throws MessagingException, IOException, TemplateException {
        String accountActivationHtml = FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfigurer.getConfiguration().getTemplate(accountActivationProperties.getEmailTemplate()),
                Map.of("activationLink", createAccountActivationLink(confirmationToken.getCode()),
                        "timeLimitInMinutes", accountActivationProperties.getConfirmationTime().toMinutes())
        );
        emailService.sendHtml(user.getEmail(), "Регистрация", accountActivationHtml);
    }

    private String createAccountActivationLink(String code) {
        return String.format("%s?code=%s", accountActivationProperties.getLink(), code);
    }
}
