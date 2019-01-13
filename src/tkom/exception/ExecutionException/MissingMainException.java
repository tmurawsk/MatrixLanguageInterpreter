package tkom.exception.ExecutionException;

import tkom.Position;

public class MissingMainException extends ExecutionException {
    public MissingMainException(Position position) {
        super(position, "Missing \"main\" function");
    }
}
