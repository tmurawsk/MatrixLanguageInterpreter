package tkom.ast.expression;

import tkom.ast.Variable;
import tkom.ast.statement.Statement;

public abstract class MathExpression {
    protected Statement parentStatement;

    public MathExpression(Statement parentStatement) {
        this.parentStatement = parentStatement;
    }

    public abstract Variable evaluate();
}
