package ru.admin.repository;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.admin.enitity.User;

@Repository
public interface UserRepository extends ReactiveSortingRepository<User, Long> {
    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    Mono<Boolean> existsByEmail(String email);
    Mono<User> findByEmail(String email);
}
