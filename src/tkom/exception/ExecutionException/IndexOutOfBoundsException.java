package tkom.exception.ExecutionException;

import tkom.Position;

public class IndexOutOfBoundsException extends ExecutionException {
    public IndexOutOfBoundsException(Position position, int index) {
        super(position, "Index out of bound: " + index);
    }
}
