package tkom.exception;

import tkom.Token;

public class UnexpectedTokenException extends BaseException {

    public UnexpectedTokenException(Token token) {
        super(token.getPosition(), "Unexpected token: " + token.getValue());
    }
}
