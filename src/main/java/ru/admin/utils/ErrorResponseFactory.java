package ru.admin.utils;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.web.bind.support.WebExchangeBindException;
import ru.admin.dto.ErrorResponse;
import ru.admin.dto.FieldError;
import ru.admin.error.UserWithSameEmailAlreadyExists;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorResponseFactory {
    public static ErrorResponse defaultResponse() {
        return new ErrorResponse("body", "не все поля прошли проверку");
    }

    public static ErrorResponse from(UserWithSameEmailAlreadyExists exception) {
        ErrorResponse response = ErrorResponseFactory.defaultResponse();
        response.setFieldErrors(List.of(new FieldError("email", exception.getMessage())));
        return response;
    }

    public static ErrorResponse from(WebExchangeBindException exception) {
        ErrorResponse response = ErrorResponseFactory.defaultResponse();
        response.setFieldErrors(
                exception
                        .getFieldErrors()
                        .stream()
                        .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                        .collect(Collectors.toList())
        );
        return response;
    }

    public static ErrorResponse from(ConstraintViolationException exception) {
        ConstraintViolation<?> constraintViolation = (ConstraintViolation<?>) exception.getConstraintViolations().toArray()[0];
        ErrorResponse response = new ErrorResponse();
        response.setParam(((PathImpl) constraintViolation.getPropertyPath()).getLeafNode().getName());
        response.setMessage(constraintViolation.getMessage());
        return response;
    }
}
