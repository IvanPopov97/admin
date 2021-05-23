package ru.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.admin.validation.annotation.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserAuthorizationDto {
    @Schema(description = "Адрес электронной почты пользователя")
    @Email(message = "не похоже на реальный адрес электронной почты")
    @NotBlank(message = "не должно быть пустым")
    private String email;

    @Schema(description = "Пароль пользователя")
    @Password
    @NotBlank(message = "не должно быть пустым")
    private String password;
}
