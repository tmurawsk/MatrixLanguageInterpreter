package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;

public abstract class MathExpression {
    private Statement parentStatement;

    protected TokenID type;

    private Position position;

    MathExpression(Position position) {
        this.position = position;
        type = TokenID.Any;
    }

    public abstract Variable evaluate();

    abstract TokenID evaluateType();

    public TokenID getType() {
        if (type == TokenID.Any)
            type = evaluateType();
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
