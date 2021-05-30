package ru.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.admin.validation.annotation.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationDto {
    @Schema(description = "Email пользователя")
    @Email(message = "не похоже на реальный адрес электронной почты")
    @NotBlank(message = "не должно быть пустым")
    private String email;

    @Schema(description = "Пароль пользователя")
    @Password
    private String password;
}
