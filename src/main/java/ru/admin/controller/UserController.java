package ru.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("user")
@Tag(name = "пользователь", description = "API пользователей")
@CrossOrigin("${app-ui.url}")
public class UserController {

    private final UserService userService;
    private final ConfirmationTokenService tokenService;

    public UserController(UserService userService, ConfirmationTokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping
    @Operation(summary = "Зарегистрировать пользователя")
    private Mono<UserResponseDto> signUp(@RequestBody @Valid Mono<UserRegistrationDto> user) {
        return userService.signUp(user);
    }

    @GetMapping("activation/{userId}")
    @Operation(summary = "Отправить пользователю ссылку для активации аккаунта")
    private Mono<Void> activation(@PathVariable long userId) {
        return userService.sendActivationLink(userId);
    }

    @GetMapping("confirm")
    @Operation(summary = "Подтвердить действие")
    private Mono<ResponseEntity<Void>> confirm(@RequestParam String code) {
        return ControllerUtils.wrapByResponseEntity(
                tokenService.confirmToken(code).flatMap(userService::execute)
        );
    }
}
