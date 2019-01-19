package tkom.exception.ExecutionException;

import tkom.Position;

public class MissingMainException extends ExecutionException {
    private MissingMainException(Position position, String message) {
        super(position, message);
    }

    public MissingMainException(Position position) {
        this(position, "Missing \"main\" function");
    }

    @Override
    public ExecutionException setPosition(Position position) {
        return new MissingMainException(position, getMessage().split("\n", 2)[1]);
    }
}
