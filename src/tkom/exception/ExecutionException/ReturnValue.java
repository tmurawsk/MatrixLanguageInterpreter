package tkom.exception.ExecutionException;

import tkom.Position;

public class ReturnValue extends ExecutionException {
    public ReturnValue() {
        super(new Position(), "");
    }
}
