package tkom.ast.expression;

import tkom.TokenID;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class MultExpr extends MathExpression {
    private LinkedList<BaseMathExpr> baseMathExprs;

    private LinkedList<TokenID> multOps;

    public MultExpr(Statement parent, BaseMathExpr expr) {
        super(parent);
        baseMathExprs = new LinkedList<>();
        baseMathExprs.add(expr);
    }

    public void addMultiply(BaseMathExpr expr) {
        baseMathExprs.add(expr);
        multOps.add(TokenID.Multiply);
    }

    public void addDivide(BaseMathExpr expr) {
        baseMathExprs.add(expr);
        multOps.add(TokenID.Divide);
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
