package tkom.ast.expression;

import tkom.TokenID;

import java.util.LinkedList;

public class MathExpr {
    private LinkedList<MultExpr> multExprs;

    private LinkedList<TokenID> addOps;

    public MathExpr(MultExpr expression) {
        addOps = new LinkedList<>();
        multExprs = new LinkedList<>();
        multExprs.add(expression);
    }

    public void addPlus(MultExpr expr) {
        multExprs.add(expr);
        addOps.add(TokenID.Plus);
    }

    public void addMinus(MultExpr expr) {
        multExprs.add(expr);
        addOps.add(TokenID.Minus);
    }
}
