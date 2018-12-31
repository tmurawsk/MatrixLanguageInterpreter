package tkom.exception;

import tkom.Position;
import tkom.Token;

public class UnknownTypeException extends Exception {
    private UnknownTypeException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + message);
    }

    public UnknownTypeException(Token token) {
        this(token.getPosition(), "Error: Unknown type: " + token.getValue());
    }
}
