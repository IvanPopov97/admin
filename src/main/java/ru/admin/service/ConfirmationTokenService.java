package ru.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.enitity.ConfirmationToken;
import ru.admin.enitity.User;
import ru.admin.enitity.UserAction;
import ru.admin.error.InvalidConfirmationToken;
import ru.admin.error.TokenErrorType;
import ru.admin.repository.ConfirmationTokenRepository;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
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

    public Mono<ConfirmationToken> confirmToken(long tokenId, @NotBlank String code, @NotBlank String action) {
        return confirmationTokenRepository.findById(tokenId)
                .switchIfEmpty(Mono.error(new InvalidConfirmationToken()))
                .handle((token, sink) -> {
                    LocalDateTime now = LocalDateTime.now();
                    Optional<TokenErrorType> validationError = Optional.ofNullable(validateTokenData(token, code, action, now));
                    if (validationError.isPresent()) {
                        sink.error(new InvalidConfirmationToken(validationError.get(), token));
                    }
                    else {
                        token.setConfirmedAt(now);
                        sink.next(token);
                    }
                })
                .cast(ConfirmationToken.class)
                .flatMap(confirmationTokenRepository::save);
    }

    private TokenErrorType validateTokenData(ConfirmationToken token, String code, String action, LocalDateTime now) {
        if (!token.getCode().equals(code))
            return TokenErrorType.WRONG_CODE;
        else if (!token.getAction().toString().equalsIgnoreCase(action))
            return TokenErrorType.WRONG_ACTION;
        else if (now.isAfter(token.getExpiresAt()) || token.getConfirmedAt() != null)
            return TokenErrorType.EXPIRED;
        return null;
    }
}
