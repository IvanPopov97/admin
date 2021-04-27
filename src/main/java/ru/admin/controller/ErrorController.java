package ru.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import ru.admin.dto.ErrorResponse;
import ru.admin.dto.FieldError;
import ru.admin.utils.BaseMapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleBodyValidationError(WebExchangeBindException exception) {
        log.error("Ошибки в Request Body", exception);
        ErrorResponse response = new ErrorResponse();
        response.setParam("body");
        response.setMessage("не все поля прошли проверку");
        response.setFieldErrors(
                exception
                        .getFieldErrors()
                        .stream()
                        .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.toList())
        );
        return Mono.just(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleValidationError(ConstraintViolationException exception) {
        log.error("Ошибка в контроллере", exception);
        ConstraintViolation<?> constraintViolation = (ConstraintViolation<?>) exception.getConstraintViolations().toArray()[0];
        ErrorResponse response = new ErrorResponse();
        response.setParam(((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName());
        response.setMessage(constraintViolation.getMessage());
        return Mono.just(response);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ErrorResponse> handleError(Exception exception) {
        log.error("Ошибка в контроллере", exception);
        return Mono.just(BaseMapper.map(exception, ErrorResponse.class));
    }
}
