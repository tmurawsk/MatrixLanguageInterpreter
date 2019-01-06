package tkom.exception;

import tkom.Position;

public class BaseException extends Exception {
    public BaseException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + "ERROR: " + message);
    }
}
