package ru.admin.service;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.config.properties.PasswordProperties;
import ru.admin.enitity.ConfirmationToken;
import ru.admin.enitity.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

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

    public void sendAccountActivationEmail(User user, ConfirmationToken confirmationToken) {
        Mono.just(confirmationToken)
                .handle((token, sink) -> {
                    try {
                        sink.next(createAccountActivationHtml(token.getCode()));
                    }
                    catch (IOException | TemplateException e) {
                        log.error("Не удалось сгенерировать сообщение для активации аккаунта для пользователя "
                                + user.getEmail(), e);
                        sink.complete();
                    }
                })
                .cast(String.class)
                .doOnNext(sendHtml(user, "Регистрация"))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    public void sendEmailWithPassword(User userEntity) {
        Mono.just(userEntity)
                .handle((user, sink) -> {
                    try {
                        sink.next(createHtmlWithPassword(user.getPassword()));
                    }
                    catch (IOException | TemplateException e) {
                        String message = String.format("Не удалось сгенерировать сообщение с паролем %s для пользователя %s", user
                                .getPassword(), user.getEmail());
                        log.error(message, e);
                        sink.complete();
                    }
                })
                .cast(String.class)
                .doOnNext(sendHtml(userEntity, "Пароль"))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    private Consumer<String> sendHtml(User user, String subject) {
        return html -> {
            try {
                emailService.sendHtml(user.getEmail(), subject, html);
            }
            catch (MessagingException e) {
                String message = String.format("Не удалось отправить пользователю %s сгенерированное сообщение (%s): %s ", user
                        .getEmail(), subject, html);
                log.error(message, e);
            }
        };
    }

    private String createAccountActivationHtml(String code) throws IOException, TemplateException {
        // @formatter:off
        return FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfigurer.getConfiguration().getTemplate(accountActivationProperties.getEmailTemplate()),
                Map.of("activationLink", createAccountActivationLink(code),
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

    private String createAccountActivationLink(String code) {
        return String.format("%s?code=%s", accountActivationProperties.getLink(), code);
    }
}
