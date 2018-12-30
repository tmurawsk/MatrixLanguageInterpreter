package tkom.ast.expression;

import tkom.ast.statement.Statement;

public abstract class LogicExpression {
    protected Statement parentStatement;

    public LogicExpression(Statement parentStatement) {
        this.parentStatement = parentStatement;
    }

    public abstract boolean evaluate();
}
