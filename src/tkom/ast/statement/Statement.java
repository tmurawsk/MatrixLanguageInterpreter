package tkom.ast.statement;

import tkom.Position;
import tkom.exception.ExecutionException.ExecutionException;

public abstract class Statement {
    protected Position position;

    public Statement(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public abstract void execute() throws ExecutionException;
}
