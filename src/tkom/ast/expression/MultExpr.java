package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class MultExpr extends MathExpression {
    private LinkedList<BaseMathExpr> baseMathExprs;

    private LinkedList<TokenID> multOps;

    public MultExpr(Statement parent, Position position, BaseMathExpr expr) {
        super(parent, position);
        multOps = new LinkedList<>();
        baseMathExprs = new LinkedList<>();
        baseMathExprs.add(expr);
        type = expr.getType();
    }

    public void addMultiply(BaseMathExpr expr) {
        baseMathExprs.add(expr);
        multOps.add(TokenID.Multiply);
        updateType(expr);
    }

    public void addDivide(BaseMathExpr expr) {
        baseMathExprs.add(expr);
        multOps.add(TokenID.Divide);
        updateType(expr);
    }

    private void updateType(BaseMathExpr expr) {
        if (type == TokenID.Mat || expr.getType() == TokenID.Mat)
            type = TokenID.Mat;
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
