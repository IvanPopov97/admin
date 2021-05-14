package ru.admin.service;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.reactive.result.view.freemarker.FreeMarkerConfigurer;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.admin.config.properties.AccountActivationProperties;
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

    public UserEmailService(EmailService emailService, FreeMarkerConfigurer freemarkerConfigurer,
            AccountActivationProperties accountActivationProperties) {
        this.emailService = emailService;
        this.freemarkerConfigurer = freemarkerConfigurer;
        this.accountActivationProperties = accountActivationProperties;
    }

    public void sendAccountActivationEmail(User user, ConfirmationToken confirmationToken) {
        Mono.just(confirmationToken).publishOn(Schedulers.boundedElastic()).handle((token, sink) -> {
            try {
                sink.next(createAccountActivationHtml(token.getCode()));
            }
            catch (IOException | TemplateException e) {
                log.error("Не удалось сгенерировать сообщение для активации аккаунта для пользователя "
                        + user.getEmail(), e);
                sink.complete();
            }
        }).cast(String.class).doOnNext(html -> {
            try {
                emailService.sendHtml(user.getEmail(), "Регистрация", html);
            }
            catch (MessagingException e) {
                log.error(String.format("Не удалось отправить пользователю %s сгенерированное для активации аккаунта сообщение: %s ", user.getEmail(), html), e);
            }
        }).subscribe();
    }

    private String createAccountActivationHtml(String code) throws IOException, TemplateException {
        return FreeMarkerTemplateUtils.processTemplateIntoString(
                freemarkerConfigurer.getConfiguration().getTemplate(accountActivationProperties.getEmailTemplate()),
                Map.of("activationLink", createAccountActivationLink(code),
                        "timeLimitInMinutes", accountActivationProperties.getConfirmationTime().toMinutes())
        );
    }

    private String createAccountActivationLink(String code) {
        return String.format("%s?code=%s", accountActivationProperties.getLink(), code);
    }
}
