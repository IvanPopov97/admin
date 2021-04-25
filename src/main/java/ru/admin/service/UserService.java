package ru.admin.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.admin.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<Boolean> existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
