package tkom.exception;

import tkom.Position;
import tkom.Token;

public class UnexpectedTokenException extends Exception {
    private UnexpectedTokenException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t");
    }

    public UnexpectedTokenException(Token token) {
        this(token.getPosition(), "Error: Unexpected token: " + token.getValue());
    }
}
