package tkom.exception.ParseException;

import tkom.Position;

public abstract class ParseException extends Exception {
    ParseException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + "ERROR: " + message);
    }
}
