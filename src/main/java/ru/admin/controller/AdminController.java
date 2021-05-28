package ru.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.admin.dto.UserResponseDto;
import ru.admin.service.AdminService;
import ru.admin.utils.ControllerUtils;

@RestController
@RequestMapping("admin/user")
@Tag(name = "администратор", description = "API администратора")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("exists")
    @Operation(summary = "Проверить, существует ли пользователь с данной электронной почтой")
    private Mono<Boolean> exists(@RequestParam String email) {
        return adminService.existsByEmail(email);
    }

    @GetMapping
    @Operation(summary = "Получить данные пользователя с указанной электронной почтой")
    private Mono<ResponseEntity<UserResponseDto>> getWithEmail(@RequestParam String email) {
        return ControllerUtils.wrapByResponseEntity(adminService.getUserWithEmail(email));
    }
}
