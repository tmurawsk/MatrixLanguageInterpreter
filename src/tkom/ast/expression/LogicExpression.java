package tkom.ast.expression;

import tkom.Position;

public abstract class LogicExpression {
    private Position position;

    LogicExpression(Position position) {
        this.position = position;
    }

    public abstract boolean evaluate();

    public Position getPosition() {
        return position;
    }
}
