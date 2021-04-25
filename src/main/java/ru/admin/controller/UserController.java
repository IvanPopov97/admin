package ru.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.admin.dto.UserResponseDto;
import ru.admin.service.UserService;
import ru.admin.utils.ControllerUtils;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    private UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("exists")
    private Mono<Boolean> exists(@RequestParam String email) {
        return userService.existsByEmail(email);
    }

    @GetMapping
    private Mono<ResponseEntity<UserResponseDto>> getWithEmail(@RequestParam String email) {
        return ControllerUtils.wrapByResponseEntity(userService.getWithEmail(email));
    }
}
