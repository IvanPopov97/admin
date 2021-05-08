package ru.admin.dto;

import lombok.Data;
import ru.admin.validation.annotation.Password;

import javax.validation.constraints.Email;

@Data
public class UserRequestDto {
    private Long id;
    @Email(message = "это не похоже на реальный адрес электронной почты")
    private String email;
    //@Min(value = 8)
    //@Max(value = 50)
    //@NotBlank
    @Password
    private String password;

    public UserRequestDto withoutId() {
        this.id = null;
        return this;
    }

    public UserRequestDto withPassword(String password) {
        this.password = password;
        return this;
    }
}
