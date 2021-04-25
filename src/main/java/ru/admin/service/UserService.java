package ru.admin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.admin.dto.UserRequestDto;
import ru.admin.dto.UserResponseDto;
import ru.admin.enitity.User;
import ru.admin.repository.UserRepository;
import ru.admin.utils.BaseMapper;

import javax.validation.constraints.Email;
import java.util.Optional;

@Service
@Validated
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<Boolean> existsByEmail(@Email String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<UserResponseDto> getWithEmail(@Email String email) {
        return userRepository.findByEmail(email).map(user -> BaseMapper.map(user, UserResponseDto.class));
    }

    public Mono<UserResponseDto> create(Mono<UserRequestDto> userDto) {
        return userDto
                .map(UserRequestDto::withoutId)
                .map(this::encodePassword)
                .publishOn(Schedulers.parallel())
                .map(dto -> BaseMapper.map(dto, User.class))
                .flatMap(userRepository::save)
                .map(user -> BaseMapper.map(user, UserResponseDto.class));
    }

    private UserRequestDto encodePassword(UserRequestDto userDto) {
        return userDto.withPassword(encodePassword(userDto.getPassword()));
    }

    private String encodePassword(String password) {
        return Optional.ofNullable(password).map(passwordEncoder::encode).orElse(null);
    }
}
