package tkom.exception;

import tkom.Token;

public class UnknownTypeException extends ParseException {

    public UnknownTypeException(Token token) {
        super(token.getPosition(), "Error: Unknown type: " + token.getValue());
    }
}
