package ru.admin.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserRequestDto {
    private Long id;
    //@Email(message = "Почта указана неверно")
    @Email
    private String email;
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
