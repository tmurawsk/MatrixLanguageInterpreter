package tkom.exception.ExecutionException;

import tkom.Position;

public class ReturnValue extends ExecutionException {
    public ReturnValue() {
        super(new Position(), "");
    }

    @Override
    public ExecutionException setPosition(Position position) {
        return null; // no need to implement - no use
    }
}
