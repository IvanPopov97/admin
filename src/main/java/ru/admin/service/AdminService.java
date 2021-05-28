package ru.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;
import ru.admin.dto.UserResponseDto;
import ru.admin.repository.UserRepository;
import ru.admin.utils.BaseMapper;

import javax.validation.constraints.Email;

@Service
@Validated
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Boolean> existsByEmail(@Email String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<UserResponseDto> getUserWithEmail(@Email String email) {
        return userRepository.findByEmail(email).map(user -> BaseMapper.map(user, UserResponseDto.class));
    }
}
