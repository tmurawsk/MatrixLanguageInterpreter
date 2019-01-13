package tkom.exception.ExecutionException;

import tkom.Position;

public class ExecutionException extends Exception {
    ExecutionException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + "ERROR: " + message);
    }
}
