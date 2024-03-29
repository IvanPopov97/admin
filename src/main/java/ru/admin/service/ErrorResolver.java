package ru.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import ru.admin.dto.ErrorResponse;
import ru.admin.error.BusinessLogicError;
import ru.admin.error.InvalidConfirmationToken;
import ru.admin.error.UserWithSameEmailAlreadyExists;
import ru.admin.error.WrongAuthorizationData;
import ru.admin.utils.BaseMapper;
import ru.admin.utils.ErrorResponseFactory;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorResolver {
    @ExceptionHandler(UserWithSameEmailAlreadyExists.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleEmailExistsError(UserWithSameEmailAlreadyExists exception) {
        log.error("Попытка добавить пользователя с уже занятой почтой", exception);
        return ErrorResponseFactory.from(exception);
    }

    @ExceptionHandler(InvalidConfirmationToken.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidConfirmationTokenError(InvalidConfirmationToken exception) {
        log.error(String.format("Пользователь не смог подтвердить действие с помощью токена %s по причине: %s", exception
                .getConfirmationToken(), exception.getReason()), exception);
        return BaseMapper.map(exception, ErrorResponse.class);
    }

    @ExceptionHandler(BusinessLogicError.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleBusinessLogicError(BusinessLogicError exception) {
        log.error("Произошла ошибка, связанная с бизнес-логикой", exception);
        return BaseMapper.map(exception, ErrorResponse.class);
    }

    @ExceptionHandler(WrongAuthorizationData.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleAuthorizationError(WrongAuthorizationData exception) {
        log.error("Неправильный " + (exception.getUser() == null ? "логин" : "пароль"), exception);
        return BaseMapper.map(exception, ErrorResponse.class);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBodyValidationError(WebExchangeBindException exception) {
        log.error("Ошибка валидации тела запроса", exception);
        return ErrorResponseFactory.from(exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParamValidationError(ConstraintViolationException exception) {
        log.error("Ошибка валидации параметров запроса", exception);
        return ErrorResponseFactory.from(exception);
    }

    @ExceptionHandler(ServerWebInputException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleParamValidationError(ServerWebInputException exception) {
        log.error("Ошибка валидации параметров запроса", exception);
        return ErrorResponseFactory.from(exception);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleError(Exception exception) {
        log.error("Ошибка", exception);
        return ErrorResponseFactory.defaultResponse();
    }
}
