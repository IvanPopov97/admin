package ru.admin.error;

import lombok.Getter;
import lombok.Setter;
import ru.admin.enitity.ConfirmationToken;

@Getter
@Setter
public class InvalidConfirmationToken extends BusinessLogicError {
    private String message = "код недействителен";
    private TokenErrorType reason;
    private ConfirmationToken confirmationToken;

    public InvalidConfirmationToken() {
        this.reason = TokenErrorType.WRONG_ID;
    }

    public InvalidConfirmationToken(TokenErrorType reason, ConfirmationToken confirmationToken) {
        this.reason = reason;
        this.confirmationToken = confirmationToken;
    }
}
