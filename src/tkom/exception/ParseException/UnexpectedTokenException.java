package tkom.exception.ParseException;

import tkom.Token;

public class UnexpectedTokenException extends ParseException {

    public UnexpectedTokenException(Token token) {
        super(token.getPosition(), "Unexpected token: " + token.getValue());
    }
}
