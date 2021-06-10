package ru.admin.utils;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import ru.admin.dto.ErrorResponse;
import ru.admin.dto.FieldError;
import ru.admin.error.UserWithSameEmailAlreadyExists;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ErrorResponseFactory {

    public static ErrorResponse defaultResponse() {
        return new ErrorResponse("что-то пошло не так");
    }

    public static ErrorResponse bodyError() {
        return new ErrorResponse("body", "не все поля прошли проверку");
    }

    public static ErrorResponse from(UserWithSameEmailAlreadyExists exception) {
        ErrorResponse response = ErrorResponseFactory.bodyError();
        response.setErrors(List.of(new FieldError("email", exception.getMessage())));
        return response;
    }

    public static ErrorResponse from(WebExchangeBindException exception) {
        ErrorResponse response = ErrorResponseFactory.bodyError();
        // @formatter:off
        response.setErrors(
                exception.getFieldErrors().stream()
                        .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.toList())
        );
        // @formatter:on
        return response;
    }

    public static ErrorResponse from(ConstraintViolationException exception) {
        ConstraintViolation<?> constraintViolation = (ConstraintViolation<?>) exception.getConstraintViolations().toArray()[0];
        ErrorResponse response = new ErrorResponse();
        response.setParam(((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName());
        response.setMessage(constraintViolation.getMessage());
        return response;
    }

    public static ErrorResponse from(ServerWebInputException exception) {
        String param = Optional.ofNullable(exception.getMethodParameter())
                .map(MethodParameter::getParameterName)
                .orElse("неизвестный параметр");
        String message = Optional.ofNullable(exception.getReason())
                .map(String::toLowerCase)
                .orElse("Что-то пошло не так");
        return new ErrorResponse(param, message);
    }
}
