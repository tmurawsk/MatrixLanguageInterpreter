package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.exception.ExecutionException.ExecutionException;

public abstract class MathExpression {
    protected TokenID type;

    private Position position;

    MathExpression(Position position) {
        this.position = position;
        type = TokenID.Any;
    }

    public abstract Variable evaluate() throws ExecutionException;

    abstract TokenID evaluateType();

    public TokenID getType() {
        if (type == TokenID.Any)
            type = evaluateType();
        return type;
    }

    public Position getPosition() {
        return position;
    }
}
