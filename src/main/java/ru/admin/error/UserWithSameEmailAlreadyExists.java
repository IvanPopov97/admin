package ru.admin.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserWithSameEmailAlreadyExists extends RuntimeException {
    private String message = "пользователь с такой почтой уже существует";
    private String email;

    public UserWithSameEmailAlreadyExists(String email) {
        this.message = String.format("пользователь с почтой %s уже существует", email);
        this.email = email;
    }
}
