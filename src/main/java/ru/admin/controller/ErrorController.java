package ru.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import ru.admin.dto.ErrorResponse;
import ru.admin.utils.BaseMapper;
import ru.admin.utils.ErrorResponseFactory;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleBodyValidationError(WebExchangeBindException exception) {
        log.error("Ошибки в Request Body", exception);
        return Mono.just(exception).map(ErrorResponseFactory::from);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleValidationError(ConstraintViolationException exception) {
        log.error("Ошибка в контроллере", exception);
        return Mono.just(exception).map(ErrorResponseFactory::from);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleError(Exception exception) {
        log.error("Ошибка в контроллере", exception);
        return Mono.just(BaseMapper.map(exception, ErrorResponse.class));
    }
}
