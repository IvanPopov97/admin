package ru.admin.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.enitity.ConfirmationToken;
import ru.admin.enitity.User;
import ru.admin.enitity.UserAction;
import ru.admin.error.TokenExpired;
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

    public Mono<ConfirmationToken> createForUser(User user, UserAction action) {
        return Mono.just(user.getId()).map(userId -> {
            LocalDateTime now = LocalDateTime.now();
            return ConfirmationToken.builder()
                    .code(UUID.randomUUID().toString())
                    .createdAt(now)
                    .expiresAt(now.plusSeconds(accountActivationProperties.getConfirmationTime().toSeconds()))
                    .userId(user.getId())
                    .action(action)
                    .build();
        }).flatMap(confirmationTokenRepository::save).retry(2);
    }

    public Mono<ConfirmationToken> confirmToken(String code) {
        return confirmationTokenRepository.findByCode(code).handle((token, sink) -> {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(token.getExpiresAt()) && token.getConfirmedAt() == null) {
                token.setConfirmedAt(now);
                sink.next(token);
            }
            else
                sink.error(new TokenExpired(token));
        }).cast(ConfirmationToken.class).flatMap(confirmationTokenRepository::save);
    }
}
