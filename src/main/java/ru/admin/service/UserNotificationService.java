package ru.admin.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.admin.enitity.User;

@Service
public class UserNotificationService {
    public final UserEmailService userEmailService;

    public UserNotificationService(UserEmailService userEmailService) {
        this.userEmailService = userEmailService;
    }

    @RabbitListener(queues = "${password.generation.queue-name}")
    @Async
    public void activateUserAccount(User userEntity) {
        Mono.just(userEntity).doOnNext(userEmailService::sendEmailWithPassword).subscribe();
    }
}
