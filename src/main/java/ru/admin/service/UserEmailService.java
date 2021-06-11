package ru.admin.service;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.config.properties.PasswordProperties;
import ru.admin.enitity.ConfirmationToken;
import ru.admin.enitity.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class UserEmailService {

    private final EmailService emailService;
    private final FreeMarkerConfigurer freemarkerConfigurer;
    private final AccountActivationProperties accountActivationProperties;
    private final PasswordProperties passwordProperties;

    public UserEmailService(EmailService emailService, FreeMarkerConfigurer freemarkerConfigurer,
            AccountActivationProperties accountActivationProperties, PasswordProperties passwordProperties) {
        this.emailService = emailService;
        this.freemarkerConfigurer = freemarkerConfigurer;
        this.accountActivationProperties = accountActivationProperties;
        this.passwordProperties = passwordProperties;
    }

    public void sendAccountActivationEmail(User user, ConfirmationToken token) {
        try {
            String html = createAccountActivationHtml(token);
            sendHtml(html, user, "Регистрация");
        }
        catch (IOException | TemplateException e) {
            String message = "Не удалось сгенерировать сообщение для активации аккаунта для пользователя " + user.getEmail();
            log.error(message, e);
        }
    }

    public void sendEmailWithPassword(User user) {
        try {
            String html = createHtmlWithPassword(user.getPassword());
            sendHtml(html, user, "Пароль");
        }
        catch (IOException | TemplateException e) {
            String message = "Не удалось сгенерировать сообщение с паролем для пользователя" + user.getEmail();
            log.error(message, e);
        }
    }

    private void sendHtml(String html, User user, String subject) {
        try {
            emailService.sendHtml(user.getEmail(), subject, html);
        }
        catch (MessagingException e) {
            String message = String.format("Не удалось отправить пользователю %s сгенерированное сообщение (%s): %s ", user
                    .getEmail(), subject, html);
            log.error(message, e);
        }
    }

    private String createAccountActivationHtml(ConfirmationToken token) throws IOException, TemplateException {
        // @formatter:off
        return FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfigurer.getConfiguration().getTemplate(accountActivationProperties.getEmailTemplate()),
                Map.of("activationLink", createAccountActivationLink(token),
                        "timeLimitInMinutes", accountActivationProperties.getConfirmationTime().toMinutes())
        );
        // @formatter:on
    }

    private String createHtmlWithPassword(String password) throws IOException, TemplateException {
        // @formatter:off
        return FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfigurer.getConfiguration().getTemplate(passwordProperties.getGeneration().getEmailTemplate()),
                Map.of("password", password)
        );
        // @formatter:on
    }

    private String createAccountActivationLink(ConfirmationToken token) {
        // @formatter:off
        return String.format("%s?id=%d&code=%s&action=%s", accountActivationProperties.getLink(), token.getId(),
                token.getCode(), token.getAction().toString());
        // @formatter:on
    }
}
