package tkom.ast.expression;

import tkom.Position;
import tkom.ast.statement.Statement;

public abstract class LogicExpression {
    protected Statement parentStatement;

    private Position position;

    public LogicExpression(Statement parentStatement, Position position) {
        this.parentStatement = parentStatement;
        this.position = position;
    }

    public abstract boolean evaluate();

    public Position getPosition() {
        return position;
    }
}
