package ru.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.admin.dto.UserRegistrationDto;
import ru.admin.dto.UserResponseDto;
import ru.admin.dto.xmlparsing.Package;
import ru.admin.service.ConfirmationTokenService;
import ru.admin.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("user")
@Tag(name = "пользователь", description = "API пользователей")
@CrossOrigin
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

    @GetMapping("confirm")
    @Operation(summary = "Подтвердить действие")
    private Mono<Void> confirm(@RequestParam long id, @RequestParam String code,
            @RequestParam String action) {
        return tokenService.confirmToken(id, code, action).flatMap(userService::execute);
    }

    @PostMapping(value = "pushNotification")
    @ResponseBody
    public String push(@RequestBody Package requestPackage) {
        System.out.println(LocalDate.now() + " - " + requestPackage);
        return "{'result': 'ok'}";
    }
}
