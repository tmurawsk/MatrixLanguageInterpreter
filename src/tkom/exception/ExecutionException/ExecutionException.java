package tkom.exception.ExecutionException;

import tkom.Position;

public abstract class ExecutionException extends Exception {
    ExecutionException(Position position, String message) {
        super("In line: " + position.lineNum + ", " + position.charNum + ":\n\t"
                + "ERROR: " + message);
    }

    public abstract ExecutionException setPosition (Position position);
}
