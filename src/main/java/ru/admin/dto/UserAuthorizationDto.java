package ru.admin.dto;

import lombok.Data;
import ru.admin.validation.annotation.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserAuthorizationDto {
    @Email(message = "не похоже на реальный адрес электронной почты")
    @NotBlank(message = "не должно быть пустым")
    private String email;

    @Password
    @NotBlank(message = "не должно быть пустым")
    private String password;
}
