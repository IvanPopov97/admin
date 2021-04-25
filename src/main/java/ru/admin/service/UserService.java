package ru.admin.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.admin.dto.UserResponseDto;
import ru.admin.repository.UserRepository;
import ru.admin.utils.BaseMapper;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<UserResponseDto> getWithEmail(String email) {
        return userRepository.findByEmail(email).map(user -> BaseMapper.map(user, UserResponseDto.class));
    }
}
