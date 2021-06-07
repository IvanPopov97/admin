package ru.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import ru.admin.dto.UserResponseDto;
import ru.admin.repository.UserRepository;
import ru.admin.utils.BaseMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Service
@Validated
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Boolean> existsByEmail(@NotBlank @Email String email) {
        return userRepository.existsByEmail(email.toLowerCase());
    }

    public Mono<UserResponseDto> getUserWithEmail(@NotBlank @Email String email) {
        return userRepository.findByEmail(email.toLowerCase()).map(user -> BaseMapper.map(user, UserResponseDto.class));
    }
}
