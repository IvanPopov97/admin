package ru.admin.dto;

import lombok.Data;
import ru.admin.validation.annotation.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRegistrationDto {
    @Email(message = "не похоже на реальный адрес электронной почты")
    @NotBlank(message = "не должно быть пустым")
    private String email;

    @Password
    private String password;

    public UserRegistrationDto withPassword(String password) {
        this.password = password;
        return this;
    }
}
