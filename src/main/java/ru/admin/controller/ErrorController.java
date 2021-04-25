package ru.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import ru.admin.dto.ErrorResponse;
import ru.admin.dto.FieldError;
import ru.admin.utils.BaseMapper;
import ru.admin.utils.ControllerUtils;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationError(WebExchangeBindException exception) {
        log.error("Ошибки в Request Body", exception);
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Не все поля прошли проверку");
        response.setFieldErrors(
                exception
                        .getFieldErrors()
                        .stream()
                        .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.toList())
        );
        return ControllerUtils.wrapErrorByResponseEntity(response);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleError(Exception exception) {
        log.error("Ошибка в контроллере", exception);
        return ControllerUtils.wrapErrorByResponseEntity(BaseMapper.map(exception, ErrorResponse.class));
    }
}
