package ru.admin.repository;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.admin.enitity.ConfirmationToken;

@Repository
public interface ConfirmationTokenRepository extends ReactiveSortingRepository<ConfirmationToken, Long> {
    Mono<ConfirmationToken> findByCode(String code);
}
