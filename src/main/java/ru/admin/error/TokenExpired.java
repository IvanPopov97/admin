package ru.admin.error;

import lombok.Getter;
import lombok.Setter;
import ru.admin.enitity.ConfirmationToken;

@Getter
@Setter
public class TokenExpired extends BusinessLogicError{
    private ConfirmationToken token;
    private String message = "код недействителен";

    public TokenExpired(ConfirmationToken token) {
        this.token = token;
    }
}
