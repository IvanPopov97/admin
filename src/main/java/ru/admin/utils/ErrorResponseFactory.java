package ru.admin.utils;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.web.bind.support.WebExchangeBindException;
import ru.admin.dto.ErrorResponse;
import ru.admin.dto.FieldError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

public class ErrorResponseFactory {
    public static ErrorResponse from(WebExchangeBindException exception) {
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
