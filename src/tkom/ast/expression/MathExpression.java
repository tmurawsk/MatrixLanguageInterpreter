package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;

public abstract class MathExpression {
    private Statement parentStatement;

    protected TokenID type;

    private Position position;

    MathExpression(Statement parentStatement, Position position) {
        this.parentStatement = parentStatement;
        this.position = position;
        type = TokenID.Invalid;
    }

    public abstract Variable evaluate();

    public TokenID getType() {
        return type;
    }

    public Statement getParent() {
        return parentStatement;
    }

    public void setParent(Statement parentStatement) {
        this.parentStatement = parentStatement;
    }

    public Position getPosition() {
        return position;
    }
}
