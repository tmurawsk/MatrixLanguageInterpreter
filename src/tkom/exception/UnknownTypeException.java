package tkom.exception;

import tkom.Token;

public class UnknownTypeException extends ParseException {

    public UnknownTypeException(Token token) {
        super(token.getPosition(), "Unknown type: " + token.getValue());
    }
}
