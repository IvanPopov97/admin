package ru.admin.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.admin.config.PasswordGeneratorTemplate;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.config.properties.PasswordProperties;
import ru.admin.dto.UserRegistrationDto;
import ru.admin.dto.UserResponseDto;
import ru.admin.enitity.ConfirmationToken;
import ru.admin.enitity.User;
import ru.admin.enitity.UserStatus;
import ru.admin.error.UserWithSameEmailAlreadyExists;
import ru.admin.repository.UserRepository;
import ru.admin.utils.BaseMapper;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGeneratorTemplate passwordGeneratorTemplate;
    private final AmqpTemplate messagingTemplate;
    private final AccountActivationProperties accountActivationProperties;
    private final PasswordProperties passwordProperties;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            PasswordGeneratorTemplate passwordGeneratorTemplate, AmqpTemplate messagingTemplate,
            AccountActivationProperties accountActivationProperties, PasswordProperties passwordProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordGeneratorTemplate = passwordGeneratorTemplate;
        this.messagingTemplate = messagingTemplate;
        this.accountActivationProperties = accountActivationProperties;
        this.passwordProperties = passwordProperties;
    }

    public Mono<UserResponseDto> signUp(Mono<UserRegistrationDto> userDto) {
        AtomicReference<String> generatedPassword = new AtomicReference<>();

        return userDto.flatMap(this::throwErrorIfEmailExists) // вернуть ошибку, если email занят
                .publishOn(Schedulers.boundedElastic()).doOnNext(dto -> {
                    if (StringUtils.isBlank(dto.getPassword())) { // если пароля нет, сгенерировать
                        dto.setPassword(passwordGeneratorTemplate.generatePassword());
                        generatedPassword.set(dto.getPassword());
                    }
                    dto.setPassword(passwordEncoder.encode(dto.getPassword()));
                }).map(dto -> BaseMapper.map(dto, User.class)).flatMap(userRepository::save).doOnNext(user -> {
                    user.setPassword(null);
                    messagingTemplate.convertAndSend(accountActivationProperties.getQueueName(), user);
                    user.setPassword(generatedPassword.get());
                    if (user.getPassword() != null) {
                        messagingTemplate.convertAndSend(passwordProperties.getGeneration().getQueueName(), user);
                    }
                }).map(user -> BaseMapper.map(user, UserResponseDto.class));
    }

    // TODO: сделать switch по token.action (точно ли надо возвращать void?)
    public Mono<Void> execute(ConfirmationToken token) {
        return activateUserAccount(token.getUserId());
    }

    public Mono<Void> activateUserAccount(long userId) {
        return userRepository.findById(userId)
                .filter(user -> user.getStatus() == UserStatus.NOT_ACTIVE)
                .doOnNext(user -> user.setStatus(UserStatus.ACTIVE))
                .flatMap(userRepository::save)
                .then();
    }

    private Mono<UserRegistrationDto> throwErrorIfEmailExists(UserRegistrationDto dto) {
        return userRepository.existsByEmail(dto.getEmail()).handle((exists, sink) -> {
            if (exists)
                sink.error(new UserWithSameEmailAlreadyExists(dto.getEmail()));
            else
                sink.next(dto);
        });
    }
}
