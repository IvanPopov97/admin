package ru.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.admin.validation.annotation.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationDto {
    @Schema(description = "Адрес электронной почты пользователя")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Пароль пользователя")
    @Password
    private String password;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
