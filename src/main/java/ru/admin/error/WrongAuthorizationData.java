package ru.admin.error;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.admin.enitity.User;

@Getter
@Setter
@NoArgsConstructor
public class WrongAuthorizationData extends BusinessLogicError {
    private String message = "неправильный логин/пароль";
    private User user;

    public WrongAuthorizationData(User user) {
        this.user = user;
    }
}
