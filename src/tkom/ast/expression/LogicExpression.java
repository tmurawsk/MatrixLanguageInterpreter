package tkom.ast.expression;

import tkom.Position;
import tkom.exception.ExecutionException.ExecutionException;

public abstract class LogicExpression {
    private Position position;

    LogicExpression(Position position) {
        this.position = position;
    }

    public abstract boolean evaluate() throws ExecutionException;

    public Position getPosition() {
        return position;
    }
}
