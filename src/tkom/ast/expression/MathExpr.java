package tkom.ast.expression;

import tkom.TokenID;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;

import java.util.LinkedList;

public class MathExpr extends MathExpression {
    private LinkedList<MultExpr> multExprs;

    private LinkedList<TokenID> addOps;

    public MathExpr(Statement parent, MultExpr expression) {
        super(parent);
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

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
