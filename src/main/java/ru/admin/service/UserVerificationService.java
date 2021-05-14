package ru.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.admin.enitity.User;


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
                .publishOn(Schedulers.boundedElastic())
                .flatMap(confirmationTokenService::createForUser)
                .doOnError(error -> log.error("Не получилось создать токен для активации аккаунта пользователя: " + userEntity.getEmail(), error))
                .doOnNext(token -> userEmailService.sendAccountActivationEmail(userEntity, token))
                .subscribe();
    }
}
