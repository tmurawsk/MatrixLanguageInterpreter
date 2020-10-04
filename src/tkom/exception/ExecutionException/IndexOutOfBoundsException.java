package tkom.exception.ExecutionException;

import tkom.Position;

public class IndexOutOfBoundsException extends ExecutionException {
    private IndexOutOfBoundsException(Position position, String message) {
        super(position, message);
    }

    public IndexOutOfBoundsException(Position position, int index) {
        this(position, "Index out of bound: " + index);
    }

    @Override
    public ExecutionException setPosition(Position position) {
        return new IndexOutOfBoundsException(position, getMessage().split("\n", 2)[1]);
    }
}
