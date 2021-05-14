package ru.admin.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.admin.config.properties.AccountActivationProperties;
import ru.admin.dto.UserRegistrationDto;
import ru.admin.dto.UserResponseDto;
import ru.admin.enitity.User;
import ru.admin.enitity.UserStatus;
import ru.admin.error.UserWithSameEmailAlreadyExists;
import ru.admin.repository.UserRepository;
import ru.admin.utils.BaseMapper;

import javax.validation.constraints.Email;

@Service
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AmqpTemplate messagingTemplate;
    private final AccountActivationProperties accountActivationProperties;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AmqpTemplate messagingTemplate,
            AccountActivationProperties accountActivationProperties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.messagingTemplate = messagingTemplate;
        this.accountActivationProperties = accountActivationProperties;
    }

    public Mono<Boolean> existsByEmail(@Email String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<UserResponseDto> getUserWithEmail(@Email String email) {
        return userRepository.findByEmail(email).map(user -> BaseMapper.map(user, UserResponseDto.class));
    }

    public Mono<UserResponseDto> signUp(Mono<UserRegistrationDto> userDto) {
        return userDto
                .flatMap(this::throwErrorIfEmailExists)
                .publishOn(Schedulers.boundedElastic())
                .map(dto -> BaseMapper.map(encodePassword(dto), User.class))
                .flatMap(userRepository::save)
                .doOnNext(user -> messagingTemplate.convertAndSend(accountActivationProperties.getQueueName(), user))
                .map(user -> BaseMapper.map(user, UserResponseDto.class));
    }

    public Mono<Void> activateUserAccount(long userId) {
        return userRepository.findById(userId)
                .filter(user -> user.getStatus() == UserStatus.NOT_ACTIVE)
                .doOnNext(user -> user.setStatus(UserStatus.ACTIVE))
                .flatMap(userRepository::save)
                .then();
    }

    private UserRegistrationDto encodePassword(UserRegistrationDto userDto) {
        return userDto.withPassword(passwordEncoder.encode(userDto.getPassword()));
    }

    private Mono<UserRegistrationDto> throwErrorIfEmailExists(UserRegistrationDto dto) {
        return userRepository.existsByEmail(dto.getEmail()).filter(exists -> !exists).map(exists -> dto)
                .switchIfEmpty(Mono.error(new UserWithSameEmailAlreadyExists(dto.getEmail())));
    }
}
