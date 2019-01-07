package tkom.exception;

import tkom.Position;

public class ParseException extends Exception {
    ParseException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + "ERROR: " + message);
    }
}
