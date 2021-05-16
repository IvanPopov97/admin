package ru.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.admin.dto.UserRegistrationDto;
import ru.admin.dto.UserResponseDto;
import ru.admin.service.ConfirmationTokenService;
import ru.admin.service.UserService;
import ru.admin.utils.ControllerUtils;

import javax.validation.Valid;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final ConfirmationTokenService tokenService;

    public UserController(UserService userService, ConfirmationTokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping("exists")
    @Operation(summary = "Проверить, существует ли пользователь с данной электронной почтой")
    private Mono<Boolean> exists(@RequestParam String email) {
        return userService.existsByEmail(email);
    }

    @GetMapping
    @Operation(summary = "Получить данные пользователя с указанной электронной почтой")
    private Mono<ResponseEntity<UserResponseDto>> getWithEmail(@RequestParam String email) {
        return ControllerUtils.wrapByResponseEntity(userService.getUserWithEmail(email));
    }

    @PostMapping("signup")
    @Operation(summary = "Зарегистрировать пользователя")
    public Mono<UserResponseDto> signUp(@RequestBody @Valid Mono<UserRegistrationDto> user) {
        return userService.signUp(user);
    }

    @GetMapping("confirm")
    @Operation(summary = "Подтвердить действие")
    public Mono<ResponseEntity<Void>> confirm(@RequestParam String code) {
        return ControllerUtils.wrapByResponseEntity(
                tokenService.confirmToken(code).flatMap(userService::execute)
        );
    }

}
