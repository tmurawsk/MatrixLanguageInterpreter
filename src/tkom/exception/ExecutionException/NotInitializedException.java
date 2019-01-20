package tkom.exception.ExecutionException;

import tkom.Position;
import tkom.ast.Variable;

public class NotInitializedException extends ExecutionException {
    private NotInitializedException(Position position, String message) {
        super(position, message);
    }

    public NotInitializedException(Position position, Variable v) {
        this(position, "Variable was not initialized: " + v.name);
    }

    @Override
    public ExecutionException setPosition(Position position) {
        return new NotInitializedException(position, getMessage().split("\n")[1]);
    }
}
