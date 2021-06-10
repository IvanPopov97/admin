package ru.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.admin.validation.annotation.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserAuthorizationDto {
    @Schema(description = "Адрес электронной почты пользователя")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Пароль пользователя")
    @Password
    @NotBlank
    private String password;
}
