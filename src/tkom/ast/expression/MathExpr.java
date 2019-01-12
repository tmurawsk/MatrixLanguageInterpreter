package tkom.ast.expression;

import tkom.Position;
import tkom.TokenID;
import tkom.ast.Variable;
import tkom.ast.statement.Statement;
import tkom.exception.TypeMismatchException;

import java.util.LinkedList;

public class MathExpr extends MathExpression {
    private LinkedList<MultExpr> multExprs;

    private LinkedList<TokenID> addOps;

    public MathExpr(Statement parent, Position position, MultExpr expression) {
        super(parent, position);
        addOps = new LinkedList<>();
        multExprs = new LinkedList<>();
        multExprs.add(expression);
        type = TokenID.Any;
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
    TokenID evaluateType() {
        return multExprs.getFirst().getType();
    }

    @Override
    public Variable evaluate() {
        return null; //TODO
    }
}
