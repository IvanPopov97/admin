package ru.admin.service;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.admin.enitity.User;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
@Slf4j
public class UserVerificationService {

    private final ConfirmationTokenService confirmationTokenService;
    public final UserEmailService userEmailService;

    public UserVerificationService(ConfirmationTokenService confirmationTokenService, UserEmailService userEmailService) {
        this.confirmationTokenService = confirmationTokenService;
        this.userEmailService = userEmailService;
    }

    @RabbitListener(queues = "${account-activation.queue-name}")
    @Async
    public void verifyUserEmail(User user) {
        confirmationTokenService
                .createForUser(Mono.just(user))
                .handle((token, sink) -> {
                    try {
                        userEmailService.sendAccountActivationEmail(user, token);
                    }
                    catch (IOException | TemplateException e) {
                        log.error("Не удалось сгенерировать сообщение для активации аккаунта", e);
                    }
                    catch (MessagingException e) {
                        log.error("Не удалось отправить сгенерированное сообщение для активации аккаунта", e);
                    }
                })
                .subscribe();
    }

}
