package ru.admin.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.admin.enitity.User;


@Service
public class UserVerificationService {

    private final ConfirmationTokenService confirmationTokenService;

    public UserVerificationService(ConfirmationTokenService confirmationTokenService) {
        this.confirmationTokenService = confirmationTokenService;
    }

    @RabbitListener(queues = "${account-activation.queue-name}")
    @Async
    public void verifyUserEmail(User user) {
        confirmationTokenService.createForUser(Mono.just(user)).subscribe();
    }

}
