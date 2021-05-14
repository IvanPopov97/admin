package ru.admin.utils;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public class ControllerUtils {
    public static <T> Mono<ResponseEntity<T>> wrapByResponseEntity(Mono<T> object) {
        return object.map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
