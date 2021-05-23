package ru.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.admin.enitity.User;
import ru.admin.enitity.UserAction;

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
    public void activateUserAccount(User userEntity) {
        Mono.just(userEntity)
                .flatMap(user -> confirmationTokenService.createForUser(user, UserAction.ACTIVATE_ACCOUNT))
                .doOnError(error -> log.error("Не получилось создать токен для активации аккаунта пользователя: "
                        + userEntity.getEmail(), error))
                .doOnNext(token -> userEmailService.sendAccountActivationEmail(userEntity, token))
                .subscribe();
    }
}
