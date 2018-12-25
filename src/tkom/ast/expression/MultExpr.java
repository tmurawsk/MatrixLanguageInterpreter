package tkom.ast.expression;

import tkom.TokenID;

import java.util.LinkedList;

public class MultExpr {
    private LinkedList<BaseMathExpr> baseMathExprs;

    private LinkedList<TokenID> multOps;

    public MultExpr(BaseMathExpr expr) {
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
}
