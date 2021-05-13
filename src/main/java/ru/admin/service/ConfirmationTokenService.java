package ru.admin.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.enitity.ConfirmationToken;
import ru.admin.enitity.User;
import ru.admin.repository.ConfirmationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AccountActivationProperties accountActivationProperties;

    public ConfirmationTokenService(ConfirmationTokenRepository confirmationTokenRepository,
            AccountActivationProperties accountActivationProperties) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.accountActivationProperties = accountActivationProperties;
    }

    public Mono<ConfirmationToken> createForUser(Mono<User> userEntity) {
        return userEntity.publishOn(Schedulers.boundedElastic())
                .map(user -> ConfirmationToken.builder()
                        .code(UUID.randomUUID().toString())
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusSeconds(accountActivationProperties.getConfirmationTime().toSeconds()))
                        .userId(user.getId())
                        .build())
                .flatMap(confirmationTokenRepository::save);
    }
}
