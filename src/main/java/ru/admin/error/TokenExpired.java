package ru.admin.error;

import lombok.Getter;
import lombok.Setter;
import ru.admin.enitity.ConfirmationToken;

@Getter
@Setter
public class TokenExpired extends RuntimeException{
    private ConfirmationToken token;

    public TokenExpired(ConfirmationToken token) {
        super("код недействителен");
        this.token = token;
    }
}
